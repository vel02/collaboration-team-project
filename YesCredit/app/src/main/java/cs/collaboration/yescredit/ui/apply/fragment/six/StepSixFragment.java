package cs.collaboration.yescredit.ui.apply.fragment.six;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.FragmentStepSixBinding;
import cs.collaboration.yescredit.ui.apply.Hostable;
import cs.collaboration.yescredit.ui.apply.fragment.validation.ViewTextWatcher;
import cs.collaboration.yescredit.ui.apply.model.LoanForm;
import cs.collaboration.yescredit.viewmodel.ViewModelProviderFactory;
import dagger.android.support.DaggerFragment;

public class StepSixFragment extends DaggerFragment {

    private static final String TAG = "StepSixFragment";

    @Inject
    ViewModelProviderFactory providerFactory;

    private FragmentStepSixBinding binding;
    private StepSixViewModel viewModel;
    private LoanForm loanForm;
    private Hostable hostable;

    private String selectedUserCivilStatus;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStepSixBinding.inflate(inflater);
        viewModel = new ViewModelProvider(this, providerFactory).get(StepSixViewModel.class);
        subscribeObservers();
        initialization();
        navigation();
        return binding.getRoot();
    }

    private void initialization() {
        List<EditText> views = Arrays.asList(binding.fragmentSixSourceOfIncome, binding.fragmentSixIncomePerMonth);

        ViewTextWatcher viewTextWatcher = new ViewTextWatcher(views, binding.fragmentSixNext, "six");
        binding.fragmentSixSourceOfIncome.addTextChangedListener(viewTextWatcher);
        binding.fragmentSixIncomePerMonth.addTextChangedListener(viewTextWatcher);
    }


    private void navigation() {

        String[] civil_status_array = requireActivity().getResources().getStringArray(R.array.civil_status_array);

        ArrayAdapter<CharSequence> adapter;

        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.civil_status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.fragmentSixCivilStatus.setAdapter(adapter);
        binding.fragmentSixCivilStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemSelected: " + civil_status_array[position]);
                selectedUserCivilStatus = civil_status_array[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.fragmentSixNext.setOnClickListener(v -> {
            enlistUserLoanInformation();
            hostable.onInflate(v, getString(R.string.tag_fragment_submit_application));
        });
    }

    private void subscribeObservers() {
        viewModel.observedLoanForm().removeObservers(getViewLifecycleOwner());
        viewModel.observedLoanForm().observe(getViewLifecycleOwner(), loanForm -> {
            if (loanForm != null) {
                StepSixFragment.this.loanForm = loanForm;
                if (!loanForm.getCivilStatus().isEmpty()) {
                    binding.fragmentSixCivilStatus.setSelection(getCivilStatus(loanForm.getCivilStatus()));
                }
                binding.fragmentSixSourceOfIncome.setText(loanForm.getSourceOfIncome());
                binding.fragmentSixIncomePerMonth.setText(loanForm.getIncomePerMonth());
            }
        });
    }

    private void enlistUserLoanInformation() {
        loanForm.setCivilStatus(selectedUserCivilStatus);
        loanForm.setSourceOfIncome(binding.fragmentSixSourceOfIncome.getText().toString());
        loanForm.setIncomePerMonth(binding.fragmentSixIncomePerMonth.getText().toString());
        hostable.onEnlist(loanForm);
    }

    private int getCivilStatus(String value) {
        switch (value.toLowerCase()) {
            case "single":
                return 0;
            case "married":
                return 1;
        }
        return 0;
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