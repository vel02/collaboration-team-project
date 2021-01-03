package cs.collaboration.yescredit.ui.account.fragment.information;

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
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.nostra13.universalimageloader.core.ImageLoader;

import javax.inject.Inject;

import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.FragmentPersonalBinding;
import cs.collaboration.yescredit.model.Address;
import cs.collaboration.yescredit.ui.account.Hostable;
import cs.collaboration.yescredit.ui.apply.dialog.GeneratePhotoFragment;
import cs.collaboration.yescredit.util.RxBackgroundImageResize;
import cs.collaboration.yescredit.viewmodel.ViewModelProviderFactory;
import dagger.android.support.DaggerFragment;

public class PersonalFragment extends DaggerFragment implements GeneratePhotoFragment.OnPhotoReceivedListener, RxBackgroundImageResize.OnExecuteUploadListener {

    @Override
    public void onExecuteUpload(byte[] bytes) {
        viewModel.uploadUserImage(bytes);
    }

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

    @Inject
    ViewModelProviderFactory providerFactory;

    private static final String TAG = "PersonalFragment";
    private static final int REQUEST_CODE = 1234;

    private FragmentPersonalBinding binding;
    private RxBackgroundImageResize resize;
    private PersonalViewModel viewModel;
    private Activity activity;
    private Hostable hostable;

    private boolean storagePermissions;
    private Uri selectedImageUri;
    private Bitmap selectedImageBitmap;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPersonalBinding.inflate(inflater);
        viewModel = new ViewModelProvider(this, providerFactory).get(PersonalViewModel.class);
        resize = new RxBackgroundImageResize(requireActivity());
        resize.setOnExecuteUploadListener(this);
        subscribeObservers();
        navigation();
        return binding.getRoot();
    }

    private void subscribeObservers() {

        viewModel.observedAddress().removeObservers(getViewLifecycleOwner());
        viewModel.observedAddress().observe(getViewLifecycleOwner(), address -> {
            if (address != null) {
                binding.fragmentPersonalAddress.setText(addressFormatter(address));
            }
        });

        viewModel.observedUser().removeObservers(getViewLifecycleOwner());
        viewModel.observedUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                binding.fragmentPersonalPhone.setText(!user.getPhone_number().isEmpty() ? user.getPhone_number() : "No available");
                ImageLoader.getInstance().displayImage(user.getProfile_image(), binding.fragmentPersonalImage);
            }
        });

        viewModel.observedUserEmail().removeObservers(getViewLifecycleOwner());
        viewModel.observedUserEmail().observe(getViewLifecycleOwner(), email -> {
            if (email != null || !email.isEmpty()) {
                binding.fragmentPersonalEmail.setText(email);
            }
        });

        viewModel.observedUserImage().removeObservers(getViewLifecycleOwner());
        viewModel.observedUserImage().observe(getViewLifecycleOwner(), uri -> {
            if (uri != null || !uri.isEmpty()) {
                ImageLoader.getInstance().displayImage(uri, binding.fragmentPersonalImage);
            }
        });

        viewModel.observedUploadNotification().removeObservers(getViewLifecycleOwner());
        viewModel.observedUploadNotification().observe(getViewLifecycleOwner(), message -> {
            if (message != null || !message.isEmpty()) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private String addressFormatter(Address address) {

        if (!address.getAddress_street().isEmpty()) {
            return address.getAddress_street() + ", " + address.getAddress_barangay()
                    + "\n" + address.getAddress_city() + "\n" + address.getAddress_province()
                    + "\n" + address.getAddress_zipcode() + " " + address.getAddress_province().toUpperCase();
        }

        return "No available";
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
        resize.execute(imageUri);
    }

    private void uploadNewPhoto(Bitmap imageBitmap) {
        resize.execute(imageBitmap);
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
        verifyStoragePermissions();
        viewModel.getUserInformation();
    }
}
