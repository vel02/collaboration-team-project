package cs.collaboration.yescredit.ui.apply.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import javax.inject.Inject;

import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.FragmentStepSixBinding;
import cs.collaboration.yescredit.ui.apply.Hostable;
import cs.collaboration.yescredit.ui.apply.SessionManager;
import cs.collaboration.yescredit.ui.apply.model.LoanForm;
import dagger.android.support.DaggerFragment;

public class StepSixFragment extends DaggerFragment {

    private static final String TAG = "StepSixFragment";

    private FragmentStepSixBinding binding;
    @Inject
    SessionManager sessionManager;

    private Hostable hostable;

    private String levelOfEducation;
    private String reason;
    private String moreDetails;
    private String outstanding;
    private String civilStatus;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStepSixBinding.inflate(inflater);
        getLoanInfo();
        navigation();
        return binding.getRoot();
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
                civilStatus = civil_status_array[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.fragmentSixNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hostable.onEnlist(loanInfo());
                hostable.onInflate(v, getString(R.string.tag_fragment_submit_application));
            }
        });
    }

    private void getLoanInfo() {
        sessionManager.observeLoanForm().removeObservers(getViewLifecycleOwner());
        sessionManager.observeLoanForm().observe(getViewLifecycleOwner(), loanForm -> {
            if (loanForm != null) {
                Log.d(TAG, "getLoanInfo: loanForm: " + loanForm);
                levelOfEducation = loanForm.getLevelOfEducation();
                reason = loanForm.getReason();
                moreDetails = loanForm.getMoreDetails();
                outstanding = loanForm.getOutstanding();
                if (loanForm.getCivilStatus() != null) {
                    binding.fragmentSixCivilStatus.setSelection(getCivilStatus(loanForm.getCivilStatus()));
                }
                binding.fragmentSixSourceOfIncome.setText(loanForm.getSourceOfIncome());
                binding.fragmentSixIncomePerMonth.setText(loanForm.getIncomePerMonth());
            }
        });
    }

    private LoanForm loanInfo() {
        LoanForm loan = new LoanForm();

        loan.setLevelOfEducation(levelOfEducation);
        loan.setReason(reason);
        loan.setMoreDetails(moreDetails);
        loan.setOutstanding(outstanding);
        loan.setCivilStatus(civilStatus);
        loan.setSourceOfIncome(binding.fragmentSixSourceOfIncome.getText().toString());
        loan.setIncomePerMonth(binding.fragmentSixIncomePerMonth.getText().toString());

        return loan;
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