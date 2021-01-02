package cs.collaboration.yescredit.ui.apply.fragment.three;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.FragmentStepThreeBinding;
import cs.collaboration.yescredit.ui.apply.Hostable;
import cs.collaboration.yescredit.ui.apply.fragment.validation.ViewTextWatcher;
import cs.collaboration.yescredit.ui.apply.model.UserForm;
import cs.collaboration.yescredit.viewmodel.ViewModelProviderFactory;
import dagger.android.support.DaggerFragment;

public class StepThreeFragment extends DaggerFragment {

    private static final String TAG = "StepThreeFragment";

    @Inject
    ViewModelProviderFactory providerFactory;

    private FragmentStepThreeBinding binding;
    private StepThreeViewModel viewModel;
    private UserForm userForm;
    private Hostable hostable;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStepThreeBinding.inflate(inflater);
        viewModel = new ViewModelProvider(this, providerFactory).get(StepThreeViewModel.class);
        subscribeObservers();
        initialization();
        navigation();
        return binding.getRoot();
    }

    private void initialization() {
        List<EditText> views = new ArrayList<>();
        views.add(binding.fragmentThreeStreetAdd);
        views.add(binding.fragmentThreeBarangayAdd);
        views.add(binding.fragmentThreeCityAdd);
        views.add(binding.fragmentThreeProvinceAdd);
        views.add(binding.fragmentThreePostalAdd);

        ViewTextWatcher viewTextWatcher = new ViewTextWatcher(views, binding.fragmentThreeNext, "three");
        binding.fragmentThreeStreetAdd.addTextChangedListener(viewTextWatcher);
        binding.fragmentThreeBarangayAdd.addTextChangedListener(viewTextWatcher);
        binding.fragmentThreeCityAdd.addTextChangedListener(viewTextWatcher);
        binding.fragmentThreeProvinceAdd.addTextChangedListener(viewTextWatcher);
        binding.fragmentThreePostalAdd.addTextChangedListener(viewTextWatcher);

    }

    private void subscribeObservers() {

        viewModel.observedUserForm().removeObservers(getViewLifecycleOwner());
        viewModel.observedUserForm().observe(getViewLifecycleOwner(), form -> {
            if (form != null) {
                StepThreeFragment.this.userForm = form;
                binding.fragmentThreeStreetAdd.setText(form.getStreet_address());
                binding.fragmentThreeBarangayAdd.setText(form.getBarangay_address());
                binding.fragmentThreeCityAdd.setText(form.getCity_address());
                binding.fragmentThreeProvinceAdd.setText(form.getProvince_address());
                binding.fragmentThreePostalAdd.setText(form.getPostal_address());
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

            enlistUserInformation(street, barangay, city, province, postal);
            hostable.onInflate(view, getString(R.string.tag_fragment_step_four));

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