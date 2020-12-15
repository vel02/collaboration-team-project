package cs.collaboration.yescredit.ui.apply.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nostra13.universalimageloader.core.ImageLoader;

import javax.inject.Inject;

import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.FragmentStepTwoBinding;
import cs.collaboration.yescredit.ui.apply.Hostable;
import cs.collaboration.yescredit.ui.apply.SessionManager;
import cs.collaboration.yescredit.ui.apply.dialog.GovernmentPhotoFragment;
import cs.collaboration.yescredit.ui.apply.model.UserForm;
import cs.collaboration.yescredit.util.BackgroundImageResize;
import dagger.android.support.DaggerFragment;

public class StepTwoFragment extends DaggerFragment implements GovernmentPhotoFragment.OnPhotoReceivedListener {

    @Override
    public void getImagePath(Uri imagePath) {
        if (!imagePath.toString().equals("")) {
            selectedImageBitmap = null;
            selectedImageUri = imagePath;
            Log.d(TAG, "getImagePath: got the image uri: " + selectedImageUri);
            ImageLoader.getInstance().displayImage(imagePath.toString(), binding.fragmentTwoImage);
            uploadNewPhoto(selectedImageUri);
        }
    }

    @Override
    public void getImageBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            selectedImageUri = null;
            selectedImageBitmap = bitmap;
            Log.d(TAG, "getImageBitmap: got the image bitmap: " + selectedImageBitmap);
            binding.fragmentTwoImage.setImageBitmap(bitmap);
            uploadNewPhoto(selectedImageBitmap);
        }
    }

    private static final String TAG = "StepTwoFragment";
    private static final int REQUEST_CODE = 1234;
    private static final double MB_THRESHOLD = 5.0;
    private static final double MB = 1000000.0;

    @Inject
    SessionManager sessionManager;

    private FragmentStepTwoBinding binding;

    private Hostable hostable;

    private boolean storagePermissions;
    private Uri selectedImageUri;
    private Bitmap selectedImageBitmap;
    private byte[] bytes;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStepTwoBinding.inflate(inflater);
        Log.d(TAG, "onCreateView: started");

        if (!storagePermissions) {
            verifyStoragePermissions();
        }

        getUserInfo();
        navigation();
        return binding.getRoot();
    }

    private void getUserInfo() {
        sessionManager.observeUserForm().removeObservers(getViewLifecycleOwner());
        sessionManager.observeUserForm().observe(getViewLifecycleOwner(), new Observer<UserForm>() {
            @Override
            public void onChanged(UserForm form) {
                if (form != null) {
                    if (form.getGovernment_id() != null)
                        ImageLoader.getInstance().displayImage(form.getGovernment_id(), binding.fragmentTwoImage);

                }
            }
        });
    }

    private void navigation() {
        binding.fragmentTwoUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (storagePermissions) {
                    GovernmentPhotoFragment dialog = new GovernmentPhotoFragment();
                    dialog.setOnPhotoReceived(StepTwoFragment.this);
                    dialog.show(requireActivity().getSupportFragmentManager(), getString(R.string.tag_dialog_fragment_government_photo));
                } else {
                    verifyStoragePermissions();
                }
            }
        });

        binding.fragmentTwoNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //call other form ....
                hostable.onInflate(v, getString(R.string.tag_fragment_step_three));
            }
        });
    }

    public void verifyStoragePermissions() {
        Log.d(TAG, "verifyPermissions: asking user for permissions.");
        if (getActivity() == null) return;

        String[] permissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                permissions[1]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                permissions[2]) == PackageManager.PERMISSION_GRANTED) {
            storagePermissions = true;
        } else {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    permissions,
                    REQUEST_CODE
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.d(TAG, "onRequestPermissionsResult: requestCode: " + requestCode);
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "onRequestPermissionsResult: User has allowed permission to access: " + permissions[0]);

            }
        }
    }

    private void uploadNewPhoto(Uri imageUri) {
        BackgroundImageResize resize = new BackgroundImageResize(this, null);
        resize.execute(imageUri);
    }

    private void uploadNewPhoto(Bitmap imageBitmap) {
        BackgroundImageResize resize = new BackgroundImageResize(this, imageBitmap);
        Uri uri = null;
        resize.execute(uri);
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public void executeUploadTask() {
        String FIREBASE_IMAGE_STORAGE = "images/users";

        final StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child(FIREBASE_IMAGE_STORAGE + "/" + FirebaseAuth.getInstance().getCurrentUser().getUid()
                        + "/government_id");

        if (bytes.length / MB < MB_THRESHOLD) {

            UploadTask uploadTask = storageReference.putBytes(bytes);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> firebaseURL = taskSnapshot.getMetadata().getReference().getDownloadUrl();

                    firebaseURL.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            if (uri != null) {
                                Toast.makeText(getActivity(), "Upload Success", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onSuccess: firebase download url : " + uri.toString());
                                UserForm form = userInfo(uri.toString());
                                hostable.onEnlist(userInfo(form.getGovernment_id()));
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "could not upload photo", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "could not upload photo", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(getActivity(), "Image is too Large", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Validation for observers
     * let the observer finished its process before proceeding
     */
    private UserForm userInfo(String governmentID) {

        UserForm info = new UserForm();
        sessionManager.observeUserForm().removeObservers(getViewLifecycleOwner());
        sessionManager.observeUserForm().observe(getViewLifecycleOwner(), new Observer<UserForm>() {
            @Override
            public void onChanged(UserForm form) {
                if (form != null) {
                    info.setLast_name(form.getLast_name());
                    info.setFirst_name(form.getFirst_name());
                    info.setMiddle_name(form.getMiddle_name());
                    info.setGender(form.getGender());
                    info.setDate_of_birth(form.getDate_of_birth());
                    info.setGovernment_id(governmentID);
                }
            }
        });

        return info;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = getActivity();
        if (!(activity instanceof Hostable)) {
            assert activity != null;
            throw new ClassCastException(activity.getClass().getSimpleName()
                    + " must implement Hostable interface.");
        }
        hostable = (Hostable) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        hostable = null;
    }

}