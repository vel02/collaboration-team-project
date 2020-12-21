package cs.collaboration.yescredit.ui.account.fragment.information;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.FragmentPersonalBinding;
import cs.collaboration.yescredit.model.User;
import cs.collaboration.yescredit.ui.account.Hostable;
import cs.collaboration.yescredit.ui.apply.dialog.GeneratePhotoFragment;
import dagger.android.support.DaggerFragment;

public class PersonalFragment extends DaggerFragment implements GeneratePhotoFragment.OnPhotoReceivedListener {

    @Override
    public void getImagePath(Uri imagePath) {
        if (!imagePath.toString().equals("")) {
            selectedImageBitmap = null;
            selectedImageUri = imagePath;
            Log.d(TAG, "getImagePath: got the image uri: " + selectedImageUri);
            ImageLoader.getInstance().displayImage(imagePath.toString(), binding.fragmentPersonalImage);
            uploadNewPhoto(selectedImageUri);
        }
    }

    @Override
    public void getImageBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            selectedImageUri = null;
            selectedImageBitmap = bitmap;
            Log.d(TAG, "getImageBitmap: got the image bitmap: " + selectedImageBitmap);
            binding.fragmentPersonalImage.setImageBitmap(bitmap);
            uploadNewPhoto(selectedImageBitmap);
        }
    }

    private static final String TAG = "PersonalFragment";
    private static final int REQUEST_CODE = 1234;
    private static final double MB_THRESHOLD = 5.0;
    private static final double MB = 1000000.0;

    private FragmentPersonalBinding binding;
    private Activity activity;
    private Hostable hostable;

    private boolean storagePermissions;
    private Uri selectedImageUri;
    private Bitmap selectedImageBitmap;
    private byte[] bytes;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPersonalBinding.inflate(inflater);
        getUserInfo();
        navigation();
        return binding.getRoot();
    }

    private void getUserInfo() {
        Log.d(TAG, "getUserInfo: started");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {

            Query query = reference.child(getString(R.string.database_node_users))
                    .orderByChild(getString(R.string.database_field_user_id_underscore))
                    .equalTo(currentUser.getUid());

            Log.d(TAG, "getUserInfo: id " + currentUser.getUid());

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.d(TAG, "onDataChange: shot " + snapshot.getChildren());
                    for (DataSnapshot singleShot : snapshot.getChildren()) {

                        User user = singleShot.getValue(User.class);
                        Log.d(TAG, "onDataChange: user " + user);
                        if (user != null) {
                            Log.d(TAG, "onDataChange: image " + user.getProfile_image());
                            ImageLoader.getInstance().displayImage(user.getProfile_image(), binding.fragmentPersonalImage);
                            binding.fragmentPersonalEmail.setText(currentUser.getEmail());
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    private void navigation() {

        binding.fragmentPersonalChangeImage.setOnClickListener(v -> {
            if (storagePermissions) {
                GeneratePhotoFragment dialog = new GeneratePhotoFragment();
                dialog.setOnPhotoReceived(PersonalFragment.this);
                dialog.show(requireActivity().getSupportFragmentManager(), getString(R.string.tag_dialog_fragment_government_photo));
            } else {
                verifyStoragePermissions();
            }
        });

        binding.fragmentPersonalPhoneAdd.setOnClickListener(v -> {
            hostable.onInflate(v, getString(R.string.tag_fragment_phone_number));
        });

        binding.fragmentPersonalAddressAdd.setOnClickListener(v -> {
            hostable.onInflate(v, getString(R.string.tag_fragment_addresses));
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


    private void uploadNewPhoto(Uri imageUri) {
        BackgroundImageResize resize = new BackgroundImageResize(null);
        resize.execute(imageUri);
    }

    private void uploadNewPhoto(Bitmap imageBitmap) {
        BackgroundImageResize resize = new BackgroundImageResize(imageBitmap);
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
                        + "/profile_image");

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
//
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user != null) {

                                    reference.child(getString(R.string.database_node_users))
                                            .child(user.getUid())
                                            .child(getString(R.string.database_field_profile_image)).setValue(uri.toString());

                                }

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

    class BackgroundImageResize extends AsyncTask<Uri, Integer, byte[]> {

        private static final String TAG = "BackgroundImageResize";
        private static final double MB_THRESHOLD = 5.0;
        private static final double MB = 1000000.0;

        Bitmap bitmap;

        public BackgroundImageResize(Bitmap bitmap) {
            if (bitmap != null) {
                this.bitmap = bitmap;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(requireActivity(), "compressing image", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected byte[] doInBackground(Uri... uris) {
            Log.d(TAG, "doInBackground: started.");

            if (bitmap == null) {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uris[0]);
                    Log.d(TAG, "doInBackground: bitmap size: megabytes: " + bitmap.getByteCount() / MB + " MB");
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground: IOException: ", e.getCause());
                }
            }

            byte[] bytes = null;
            for (int i = 1; i < 11; i++) {
                if (i == 10) {
                    Toast.makeText(requireActivity(), "That image is too large.", Toast.LENGTH_SHORT).show();
                    break;
                }
                bytes = getBytesFromBitmap(bitmap, 100 / i);
                Log.d(TAG, "doInBackground: megabytes: (" + (11 - i) + "0%) " + bytes.length / MB + " MB");
                if (bytes.length / MB < MB_THRESHOLD) {
                    return bytes;
                }
            }
            return bytes;
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            super.onPostExecute(bytes);
            setBytes(bytes);
            executeUploadTask();
        }

        // convert from bitmap to byte array
        private byte[] getBytesFromBitmap(Bitmap bitmap, int quality) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
            return stream.toByteArray();
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
        activity = getActivity();
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
    public void onResume() {
        super.onResume();
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(activity.getResources().getColor(R.color.account_base));
    }
}
