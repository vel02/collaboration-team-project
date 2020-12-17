package cs.collaboration.yescredit.ui.apply.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import javax.inject.Inject;

import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.FragmentStepFourBinding;
import cs.collaboration.yescredit.ui.apply.Hostable;
import cs.collaboration.yescredit.ui.apply.SessionManager;
import dagger.android.support.DaggerFragment;


public class StepFourFragment extends DaggerFragment {

    private static final String TAG = "StepFourFragment";

    @Inject
    SessionManager sessionManager;

    private FragmentStepFourBinding binding;
    private Hostable hostable;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStepFourBinding.inflate(inflater);
        getUserInfo();
        navigation();
        return binding.getRoot();
    }

    private void getUserInfo() {

        sessionManager.observeUserForm().removeObservers(getViewLifecycleOwner());
        sessionManager.observeUserForm().observe(getViewLifecycleOwner(), form -> {
            if (form != null) {
                String personal_info =
                        form.getLast_name() + ", " + form.getFirst_name() + " " + form.getMiddle_name()
                                + "\n" + form.getGender() + "\n" + form.getDate_of_birth();
                String address = form.getStreet_address() + ", " + form.getBarangay_address()
                        + "\n" + form.getCity_address() + "\n" + form.getProvince_address()
                        + "\n" + form.getPostal_address() + " " + form.getProvince_address().toUpperCase();
                binding.fragmentFourPersonalInfo.setText(personal_info);
                binding.fragmentFourAddress.setText(address);
            }
        });

    }

    private void navigation() {

        binding.fragmentFourPersonalInfoRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hostable.onInflate(v, getString(R.string.tag_fragment_personal_info));
            }
        });

        binding.fragmentFourAddressRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hostable.onInflate(v, getString(R.string.tag_fragment_address));
            }
        });

        binding.fragmentFourNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //load latest data from userForm
                //save it to firebase
                sessionManager.observeUserForm().removeObservers(getViewLifecycleOwner());
                sessionManager.observeUserForm().observe(getViewLifecycleOwner(), userForm -> {
                    if (userForm != null) {
                        hostable.onSaveUserInfo(userForm);
                    }
                });

                hostable.onInflate(v, getString(R.string.tag_fragment_step_five));
            }
        });

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