package cs.collaboration.yescredit.ui.apply.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import javax.inject.Inject;

import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.FragmentStepThreeBinding;
import cs.collaboration.yescredit.ui.apply.Hostable;
import cs.collaboration.yescredit.ui.apply.SessionManager;
import cs.collaboration.yescredit.ui.apply.model.UserForm;
import dagger.android.support.DaggerFragment;

public class StepThreeFragment extends DaggerFragment {

    private static final String TAG = "StepThreeFragment";

    @Inject
    SessionManager sessionManager;

    private FragmentStepThreeBinding binding;

    private Hostable hostable;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStepThreeBinding.inflate(inflater);
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
                    binding.fragmentThreeStreetAdd.setText(form.getStreet_address());
                    binding.fragmentThreeBarangayAdd.setText(form.getBarangay_address());
                    binding.fragmentThreeCityAdd.setText(form.getCity_address());
                    binding.fragmentThreeProvinceAdd.setText(form.getProvince_address());
                    binding.fragmentThreePostalAdd.setText(form.getPostal_address());
                }
            }
        });

    }

    private void navigation() {

        binding.fragmentThreeNext.setOnClickListener(view -> {

            String street = binding.fragmentThreeStreetAdd.getText().toString();
            String barangay = binding.fragmentThreeBarangayAdd.getText().toString();
            String city = binding.fragmentThreeCityAdd.getText().toString();
            String province = binding.fragmentThreeProvinceAdd.getText().toString();
            String postal = binding.fragmentThreePostalAdd.getText().toString();

            UserForm form = userInfo(street, barangay, city, province, postal);
            hostable.onEnlist(form);
            hostable.onInflate(view, getString(R.string.tag_fragment_step_four));

        });

    }

    private UserForm userInfo(String street, String barangay, String city, String province, String postal) {

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
                    info.setGovernment_id(form.getGovernment_id());
                    info.setStreet_address(street);
                    info.setBarangay_address(barangay);
                    info.setCity_address(city);
                    info.setProvince_address(province);
                    info.setPostal_address(postal);
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