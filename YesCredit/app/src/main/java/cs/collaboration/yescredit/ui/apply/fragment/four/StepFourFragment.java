package cs.collaboration.yescredit.ui.apply.fragment.four;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.FragmentStepFourBinding;
import cs.collaboration.yescredit.model.Address;
import cs.collaboration.yescredit.ui.apply.Hostable;
import cs.collaboration.yescredit.ui.apply.model.UserForm;
import cs.collaboration.yescredit.viewmodel.ViewModelProviderFactory;
import dagger.android.support.DaggerFragment;


public class StepFourFragment extends DaggerFragment {

    private static final String TAG = "StepFourFragment";

    @Inject
    ViewModelProviderFactory providerFactory;

    private FragmentStepFourBinding binding;
    private StepFourViewModel viewModel;
    private UserForm userForm;
    private Address address;
    private Hostable hostable;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStepFourBinding.inflate(inflater);
        viewModel = new ViewModelProvider(this, providerFactory).get(StepFourViewModel.class);

        subscribeObservers();
        navigation();
        return binding.getRoot();
    }

    private void subscribeObservers() {

        viewModel.observedUserForm().removeObservers(getViewLifecycleOwner());
        viewModel.observedUserForm().observe(getViewLifecycleOwner(), form -> {
            if (form != null) {
                StepFourFragment.this.userForm = form;
                String display_personal_information =
                        form.getLast_name() + ", " + form.getFirst_name() + " " + form.getMiddle_name()
                                + "\n" + form.getGender() + "\n" + form.getDate_of_birth();
                String display_personal_address = form.getStreet_address() + ", " + form.getBarangay_address()
                        + "\n" + form.getCity_address() + "\n" + form.getProvince_address()
                        + "\n" + form.getPostal_address() + " " + form.getProvince_address().toUpperCase();
                binding.fragmentFourPersonalInfo.setText(display_personal_information);
                binding.fragmentFourAddress.setText(display_personal_address);
            }
        });

        viewModel.observedInitialAddress().removeObservers(getViewLifecycleOwner());
        viewModel.observedInitialAddress().observe(getViewLifecycleOwner(), address -> {
            if (address != null) {
                StepFourFragment.this.address = address;
            }
        });

    }

    private void navigation() {

        binding.fragmentFourPersonalInfoRoot.setOnClickListener(v -> hostable.onInflate(v, getString(R.string.tag_fragment_personal_info)));

        binding.fragmentFourAddressRoot.setOnClickListener(v -> hostable.onInflate(v, getString(R.string.tag_fragment_address)));

        binding.fragmentFourNext.setOnClickListener(v -> {

            if (address != null) {
                address.setAddress_street(userForm.getStreet_address());
                address.setAddress_barangay(userForm.getBarangay_address());
                address.setAddress_city(userForm.getCity_address());
                address.setAddress_province(userForm.getProvince_address());
                address.setAddress_zipcode(userForm.getPostal_address());
                viewModel.updateInitialAddress(address);
            }
            hostable.onSaveUserInfo(userForm);
            hostable.onInflate(v, getString(R.string.tag_fragment_step_five));
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