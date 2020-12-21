package cs.collaboration.yescredit.ui.apply.dialog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import cs.collaboration.yescredit.databinding.DialogGovernmentPhotoBinding;

public class GeneratePhotoFragment extends DialogFragment {

    private static final String TAG = "GovernmentPhotoFragment";

    public static final int CAMERA_REQUEST_CODE = 5467;
    public static final int PICK_FILE_REQUEST_CODE = 8352;

    public interface OnPhotoReceivedListener {
        void getImagePath(Uri imagePath);

        void getImageBitmap(Bitmap bitmap);
    }

    public OnPhotoReceivedListener onPhotoReceived;
    private DialogGovernmentPhotoBinding binding;

    public void setOnPhotoReceived(OnPhotoReceivedListener onPhotoReceived) {
        this.onPhotoReceived = onPhotoReceived;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DialogGovernmentPhotoBinding.inflate(inflater);

        binding.dialogChoosePhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
        });

        binding.dialogOpenCamera.setOnClickListener(v -> {
            Log.d(TAG, "onClick: starting camera");
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri selectedImageUri = data.getData();
            Log.d(TAG, "onActivityResult: image: " + selectedImageUri);

            onPhotoReceived.getImagePath(selectedImageUri);
            getDialog().dismiss();
        } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "onActivityResult: done taking a photo.");

            Bitmap bitmap;
            bitmap = (Bitmap) data.getExtras().get("data");

            onPhotoReceived.getImageBitmap(bitmap);
            getDialog().dismiss();
        }
    }

}