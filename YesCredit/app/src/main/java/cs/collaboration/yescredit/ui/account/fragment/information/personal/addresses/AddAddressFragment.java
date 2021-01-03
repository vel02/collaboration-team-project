package cs.collaboration.yescredit.ui.account.fragment.information.personal.addresses;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.FragmentAddAddressBinding;
import cs.collaboration.yescredit.model.Address;
import cs.collaboration.yescredit.ui.account.Hostable;
import cs.collaboration.yescredit.viewmodel.ViewModelProviderFactory;
import dagger.android.support.DaggerFragment;

import static cs.collaboration.yescredit.util.Utility.hideSoftKeyboard;

public class AddAddressFragment extends DaggerFragment {

    private static final String TAG = "AddAddressFragment";

    @Inject
    ViewModelProviderFactory providerFactory;

    private FragmentAddAddressBinding binding;
    private AddAddressViewModel viewModel;
    private Activity activity;

    private boolean withInitialAddress;
    private Address address;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddAddressBinding.inflate(inflater);
        viewModel = new ViewModelProvider(this, providerFactory).get(AddAddressViewModel.class);
        navigation();
        subscribeObservers();
        return binding.getRoot();
    }

    private void subscribeObservers() {
        viewModel.observedAddress().removeObservers(getViewLifecycleOwner());
        viewModel.observedAddress().observe(getViewLifecycleOwner(), address -> {
            if (address != null) {
                withInitialAddress = true;
                AddAddressFragment.this.address = address;
            }
        });
    }

    private void navigation() {

        binding.fragmentAdd.setOnClickListener(v -> {

            if (!binding.fragmentAddStreet.getText().toString().isEmpty()
                    && !binding.fragmentAddCity.getText().toString().isEmpty()) {

                String street = binding.fragmentAddStreet.getText().toString();
                String barangay = binding.fragmentAddBarangay.getText().toString().isEmpty() ? "" : binding.fragmentAddBarangay.getText().toString();
                String city = binding.fragmentAddCity.getText().toString();
                String zip = binding.fragmentAddZipcode.getText().toString().isEmpty() ? "" : binding.fragmentAddZipcode.getText().toString();
                String province = binding.fragmentAddProvince.getText().toString().isEmpty() ? "" : binding.fragmentAddProvince.getText().toString();

                if (withInitialAddress && address != null) {
                    withInitialAddress = false;

                    address.setAddress_street(street);
                    address.setAddress_barangay(barangay);
                    address.setAddress_city(city);
                    address.setAddress_zipcode(zip);
                    address.setAddress_province(province);
                    address.setAddress_status("primary");
                    address.setAddress_selected("selected");

                    viewModel.savePrimaryAddress(address);

                    reset();

                } else {
                    if (viewModel.saveOtherAddress(street, barangay, city, zip, province)) {
                        reset();
                    }
                }
            }

            if (binding.fragmentAddStreet.getText().toString().isEmpty()) {
                binding.fragmentAddStreet.requestFocus();
                binding.fragmentAddStreet.setSelection(0);
            } else if (binding.fragmentAddCity.getText().toString().isEmpty()) {
                binding.fragmentAddCity.requestFocus();
                binding.fragmentAddCity.setSelection(0);
            }

        });

    }

    private void reset() {
        hideSoftKeyboard(AddAddressFragment.this);
        requireActivity().onBackPressed();
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
    }

    @Override
    public void onResume() {
        super.onResume();
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(activity.getResources().getColor(R.color.white));
        viewModel.checkInitialAddress();
    }
}