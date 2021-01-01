package cs.collaboration.yescredit.ui.apply.fragment.four.address;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cs.collaboration.yescredit.databinding.FragmentAddressBinding;
import cs.collaboration.yescredit.ui.apply.Hostable;
import cs.collaboration.yescredit.ui.apply.fragment.validation.ViewTextWatcher;
import cs.collaboration.yescredit.ui.apply.model.UserForm;
import cs.collaboration.yescredit.viewmodel.ViewModelProviderFactory;
import dagger.android.support.DaggerFragment;

public class AddressFragment extends DaggerFragment {

    private static final String TAG = "AddressFragment";

    @Inject
    ViewModelProviderFactory providerFactory;
    private FragmentAddressBinding binding;
    private AddressViewModel viewModel;
    private UserForm userForm;
    private Hostable hostable;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddressBinding.inflate(inflater);
        viewModel = new ViewModelProvider(this, providerFactory).get(AddressViewModel.class);
        subscribeObservers();
        initialization();
        navigation();
        return binding.getRoot();
    }

    private void initialization() {
        List<EditText> views = new ArrayList<>();
        views.add(binding.fragmentAddressStreetAdd);
        views.add(binding.fragmentAddressBarangayAdd);
        views.add(binding.fragmentAddressCityAdd);
        views.add(binding.fragmentAddressProvinceAdd);
        views.add(binding.fragmentAddressPostalAdd);

        ViewTextWatcher viewTextWatcher = new ViewTextWatcher(views, binding.fragmentAddressUpdate, "three");
        binding.fragmentAddressStreetAdd.addTextChangedListener(viewTextWatcher);
        binding.fragmentAddressBarangayAdd.addTextChangedListener(viewTextWatcher);
        binding.fragmentAddressCityAdd.addTextChangedListener(viewTextWatcher);
        binding.fragmentAddressProvinceAdd.addTextChangedListener(viewTextWatcher);
        binding.fragmentAddressPostalAdd.addTextChangedListener(viewTextWatcher);

    }

    private void subscribeObservers() {

        viewModel.observedUserForm().removeObservers(getViewLifecycleOwner());
        viewModel.observedUserForm().observe(getViewLifecycleOwner(), form -> {
            if (form != null) {
                AddressFragment.this.userForm = form;
                binding.fragmentAddressStreetAdd.setText(form.getStreet_address());
                binding.fragmentAddressBarangayAdd.setText(form.getBarangay_address());
                binding.fragmentAddressCityAdd.setText(form.getCity_address());
                binding.fragmentAddressProvinceAdd.setText(form.getProvince_address());
                binding.fragmentAddressPostalAdd.setText(form.getPostal_address());
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

            enlistUserInformation(street, barangay, city, province, postal);
            requireActivity().onBackPressed();
            Toast.makeText(requireContext(), "Update Success", Toast.LENGTH_SHORT).show();
        });
    }

    private void enlistUserInformation(String street, String barangay, String city, String province, String postal) {
        userForm.setStreet_address(street);
        userForm.setBarangay_address(barangay);
        userForm.setCity_address(city);
        userForm.setProvince_address(province);
        userForm.setPostal_address(postal);
        hostable.onEnlist(userForm);
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