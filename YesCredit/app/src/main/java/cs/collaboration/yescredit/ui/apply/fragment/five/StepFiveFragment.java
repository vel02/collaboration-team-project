package cs.collaboration.yescredit.ui.apply.fragment.five;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.FragmentStepFiveBinding;
import cs.collaboration.yescredit.ui.apply.Hostable;
import cs.collaboration.yescredit.ui.apply.model.LoanForm;
import cs.collaboration.yescredit.viewmodel.ViewModelProviderFactory;
import dagger.android.support.DaggerFragment;

public class StepFiveFragment extends DaggerFragment {

    private static final String TAG = "StepFiveFragment";

    @Inject
    ViewModelProviderFactory providerFactory;

    private FragmentStepFiveBinding binding;
    private StepFiveViewModel viewModel;
    private LoanForm loanForm;
    private Hostable hostable;

    private String selectedUserLevelOfEducation;
    private String selectedUserReason;
    private String selectedUserOutstanding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStepFiveBinding.inflate(inflater);
        viewModel = new ViewModelProvider(this, providerFactory).get(StepFiveViewModel.class);
        subscribeObservers();
        navigation();
        return binding.getRoot();
    }

    private void navigation() {

        String[] education_achieved_array = requireActivity().getResources().getStringArray(R.array.education_achieved_array);
        String[] reason_loaning_array = requireActivity().getResources().getStringArray(R.array.reason_loaning_array);

        ArrayAdapter<CharSequence> adapter;

        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.education_achieved_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.fragmentFiveEducationAchieved.setAdapter(adapter);
        binding.fragmentFiveEducationAchieved.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedUserLevelOfEducation = education_achieved_array[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.reason_loaning_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.fragmentFiveReasonLoaning.setAdapter(adapter);
        binding.fragmentFiveReasonLoaning.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedUserReason = reason_loaning_array[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.fragmentFiveOutstandingGroup.setOnCheckedChangeListener((group, checkedId) -> {
            int selected = group.getCheckedRadioButtonId();
            RadioButton button = binding.getRoot().findViewById(selected);
            selectedUserOutstanding = button.getTag().toString();
            Log.d(TAG, "onCheckedChanged: outstanding: " + selectedUserOutstanding);
        });

        binding.fragmentFiveNext.setOnClickListener(v -> {

            enlistUserLoanInformation();
            hostable.onInflate(v, getString(R.string.tag_fragment_step_six));

        });

    }

    private void subscribeObservers() {
        viewModel.observedLoanForm().removeObservers(getViewLifecycleOwner());
        viewModel.observedLoanForm().observe(getViewLifecycleOwner(), loanForm -> {
            if (loanForm != null) {
                StepFiveFragment.this.loanForm = loanForm;
                binding.fragmentFiveEducationAchieved.setSelection(getLevelOfEducation(!loanForm.getLevelOfEducation().isEmpty() ? loanForm.getLevelOfEducation() : ""));
                binding.fragmentFiveReasonLoaning.setSelection(getReason(!loanForm.getReason().isEmpty() ? loanForm.getReason() : ""));
                binding.fragmentFiveDescribe.setText(!loanForm.getMoreDetails().isEmpty() ? loanForm.getMoreDetails() : "");
                if (!loanForm.getOutstanding().isEmpty()) {
                    switch (loanForm.getOutstanding().toLowerCase()) {
                        case "yes":
                            binding.fragmentFiveOutstandingYes.setChecked(true);
                            break;
                        case "no":
                            binding.fragmentFiveOutstandingNo.setChecked(true);
                            break;
                        default:
                    }
                }
            }
        });
    }

    private int getLevelOfEducation(String value) {
        switch (value.toLowerCase()) {
            case "high school graduate":
                return 0;
            case "associate degree":
                return 1;
            case "bachelor degree":
                return 2;
            case "master degree":
                return 3;
            case "professional doctorate degree":
                return 4;
        }
        return 0;
    }

    private int getReason(String value) {
        switch (value.toLowerCase()) {
            case "reason_loaning 1":
                return 0;
            case "reason_loaning 2":
                return 1;
            case "reason_loaning 3":
                return 2;
            case "reason_loaning 4":
                return 3;
        }
        return 0;
    }

    private void enlistUserLoanInformation() {

        loanForm.setLevelOfEducation(selectedUserLevelOfEducation);
        loanForm.setReason(selectedUserReason);
        loanForm.setMoreDetails(binding.fragmentFiveDescribe.getText().toString());
        loanForm.setOutstanding(selectedUserOutstanding);
        hostable.onEnlist(loanForm);

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