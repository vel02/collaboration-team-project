package cs.collaboration.yescredit.ui.apply.fragment.four;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

import cs.collaboration.yescredit.databinding.FragmentAddressBinding;
import cs.collaboration.yescredit.ui.apply.Hostable;
import cs.collaboration.yescredit.ui.apply.SessionManager;
import cs.collaboration.yescredit.ui.apply.model.ApplicationForm;
import dagger.android.support.DaggerFragment;

public class AddressFragment extends DaggerFragment {

    private static final String TAG = "AddressFragment";

    @Inject
    SessionManager sessionManager;

    private FragmentAddressBinding binding;
    private Hostable hostable;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddressBinding.inflate(inflater);
        getUserInfo();
        navigation();
        return binding.getRoot();
    }

    private void getUserInfo() {

        sessionManager.observeApplicationForm().removeObservers(getViewLifecycleOwner());
        sessionManager.observeApplicationForm().observe(getViewLifecycleOwner(), new Observer<ApplicationForm>() {
            @Override
            public void onChanged(ApplicationForm form) {
                if (form != null) {
                    binding.fragmentAddressStreetAdd.setText(form.getStreet_address());
                    binding.fragmentAddressBarangayAdd.setText(form.getBarangay_address());
                    binding.fragmentAddressCityAdd.setText(form.getCity_address());
                    binding.fragmentAddressProvinceAdd.setText(form.getProvince_address());
                    binding.fragmentAddressPostalAdd.setText(form.getPostal_address());
                }
            }
        });
    }

    private void navigation() {

        binding.fragmentAddressUpdate.setOnClickListener(view -> {

            String street = binding.fragmentAddressStreetAdd.getText().toString();
            String barangay = binding.fragmentAddressBarangayAdd.getText().toString();
            String city = binding.fragmentAddressCityAdd.getText().toString();
            String province = binding.fragmentAddressProvinceAdd.getText().toString();
            String postal = binding.fragmentAddressPostalAdd.getText().toString();

            ApplicationForm form = userInfo(street, barangay, city, province, postal);
            hostable.onEnlist(form);
            Snackbar.make(view, "Update Successful!", Snackbar.LENGTH_SHORT).show();

        });
    }

    private ApplicationForm userInfo(String street, String barangay, String city, String province, String postal) {

        ApplicationForm info = new ApplicationForm();
        sessionManager.observeApplicationForm().removeObservers(getViewLifecycleOwner());
        sessionManager.observeApplicationForm().observe(getViewLifecycleOwner(), form -> {
            if (form != null) {
                info.setLast_name(form.getLast_name());
                info.setFirst_name(form.getFirst_name());
                info.setMiddle_name(form.getMiddle_name());
                info.setGender(form.getGender());
                info.setDate_of_birth(form.getDate_of_birth());
                info.setGovernment_id(form.getGovernment_id());
                info.setStreet_address(street);
                info.setBarangay_address(barangay);
                info.setCity_address(city);
                info.setProvince_address(province);
                info.setPostal_address(postal);
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