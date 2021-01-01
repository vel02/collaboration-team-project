package cs.collaboration.yescredit.ui.apply.fragment.two;

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
import androidx.lifecycle.ViewModelProvider;

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
import cs.collaboration.yescredit.ui.apply.dialog.GeneratePhotoFragment;
import cs.collaboration.yescredit.ui.apply.model.UserForm;
import cs.collaboration.yescredit.util.RxBackgroundImageResize;
import cs.collaboration.yescredit.viewmodel.ViewModelProviderFactory;
import dagger.android.support.DaggerFragment;

import static cs.collaboration.yescredit.ui.apply.fragment.two.StepTwoViewModel.UploadNotification.UPLOADED;
import static cs.collaboration.yescredit.util.Utility.getUriToDrawable;

public class StepTwoFragment extends DaggerFragment implements GeneratePhotoFragment.OnPhotoReceivedListener,
        RxBackgroundImageResize.OnExecuteUploadListener {

    @Override
    public void onExecuteUpload(byte[] bytes) {
        String FIREBASE_IMAGE_STORAGE = "images/users";

        final StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child(FIREBASE_IMAGE_STORAGE + "/" + FirebaseAuth.getInstance().getCurrentUser().getUid()
                        + "/government_id");

        if (bytes.length / MB < MB_THRESHOLD) {

            UploadTask uploadTask = storageReference.putBytes(bytes);

            uploadTask.addOnSuccessListener(taskSnapshot -> {
                Task<Uri> firebaseURL = taskSnapshot.getMetadata().getReference().getDownloadUrl();

                firebaseURL.addOnSuccessListener(uri -> {
                    if (uri != null) {
                        Toast.makeText(getActivity(), "Upload Success", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onSuccess: firebase download url : " + uri.toString());
                        viewModel.updateUploadNotification(UPLOADED);
                        StepTwoFragment.this.userForm.setGovernment_id(uri.toString());
                        hostable.onEnlist(userForm);
                    }
                }).addOnFailureListener(e ->
                        Toast.makeText(getActivity(), "could not upload photo", Toast.LENGTH_SHORT).show()
                );
            }).addOnFailureListener(e ->
                    Toast.makeText(getActivity(), "could not upload photo", Toast.LENGTH_SHORT).show()
            );
        } else {
            Toast.makeText(getActivity(), "Image is too Large", Toast.LENGTH_SHORT).show();
        }
    }

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
    ViewModelProviderFactory providerFactory;
    @Inject
    RxBackgroundImageResize resize;

    private FragmentStepTwoBinding binding;
    private StepTwoViewModel viewModel;
    private UserForm userForm;

    private Hostable hostable;

    private boolean storagePermissions;
    private Uri selectedImageUri;
    private Bitmap selectedImageBitmap;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStepTwoBinding.inflate(inflater);
        viewModel = new ViewModelProvider(this, providerFactory).get(StepTwoViewModel.class);
        resize.setOnExecuteUploadListener(this);

        if (!storagePermissions) {
            verifyStoragePermissions();
        }

        subscribeObservers();
        navigation();
        return binding.getRoot();
    }

    private void uploadNewPhoto(Uri imageUri) {
        resize.execute(imageUri);
    }

    private void uploadNewPhoto(Bitmap imageBitmap) {
        resize.execute(imageBitmap);
    }

    private void subscribeObservers() {
        viewModel.observedUserForm().removeObservers(getViewLifecycleOwner());
        viewModel.observedUserForm().observe(getViewLifecycleOwner(), form -> {
            if (form != null) {
                StepTwoFragment.this.userForm = form;
                if (!form.getGovernment_id().isEmpty())
                    ImageLoader.getInstance().displayImage(form.getGovernment_id(), binding.fragmentTwoImage);
            }
        });

        viewModel.observedUploadNotification().removeObservers(getViewLifecycleOwner());
        viewModel.observedUploadNotification().observe(getViewLifecycleOwner(), notification -> {
            if (notification != null) {
                switch (notification) {
                    case UPLOADED:
                        binding.fragmentTwoNext.setEnabled(true);
                        break;
                    case UPLOADING:
                        binding.fragmentTwoNext.setEnabled(false);
                        binding.fragmentTwoImage.setImageURI(getUriToDrawable(requireContext(), R.drawable.ic_placeholder_id));
                        break;
                }
            }
        });
    }


    private void navigation() {
        binding.fragmentTwoUpload.setOnClickListener(v -> {
            if (storagePermissions) {
                GeneratePhotoFragment dialog = new GeneratePhotoFragment();
                dialog.setOnPhotoReceived(StepTwoFragment.this);
                dialog.show(requireActivity().getSupportFragmentManager(), getString(R.string.tag_dialog_fragment_government_photo));
            } else {
                verifyStoragePermissions();
            }
        });

        binding.fragmentTwoNext.setOnClickListener(v ->
                hostable.onInflate(v, getString(R.string.tag_fragment_step_three)));
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: called");
    }
}