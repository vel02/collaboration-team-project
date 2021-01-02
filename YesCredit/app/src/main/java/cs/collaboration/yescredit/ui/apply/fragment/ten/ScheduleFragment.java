package cs.collaboration.yescredit.ui.apply.fragment.ten;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

import javax.inject.Inject;

import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.FragmentScheduleBinding;
import cs.collaboration.yescredit.model.Code;
import cs.collaboration.yescredit.ui.apply.Hostable;
import cs.collaboration.yescredit.ui.apply.fragment.ten.model.Due;
import cs.collaboration.yescredit.ui.apply.model.LoanForm;
import cs.collaboration.yescredit.viewmodel.ViewModelProviderFactory;
import dagger.android.support.DaggerFragment;

public class ScheduleFragment extends DaggerFragment {

    private static final String TAG = "ScheduleFragment";

    @Inject
    ViewModelProviderFactory providerFactory;

    private FragmentScheduleBinding binding;
    private ScheduleViewModel viewModel;
    private LoanForm loanForm;
    private Hostable hostable;

    private Due thirtyDayDue;
    private Due fourteenDayDue;
    private int benefit;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentScheduleBinding.inflate(inflater);
        viewModel = new ViewModelProvider(this, providerFactory).get(ScheduleViewModel.class);
        subscribeObservers();
        navigation();
        return binding.getRoot();
    }

    private void subscribeObservers() {
        viewModel.observedLoanForm().removeObservers(getViewLifecycleOwner());
        viewModel.observedLoanForm().observe(getViewLifecycleOwner(), loanForm -> {
            if (loanForm != null) {
                ScheduleFragment.this.loanForm = loanForm;
                loanForm.setRepayment_days("30");
                displayDueDate(loanForm.getRepayment_loan());
            }
        });

        viewModel.observedThirtyDayDue().removeObservers(getViewLifecycleOwner());
        viewModel.observedThirtyDayDue().observe(getViewLifecycleOwner(), due -> {
            if (due != null) {
                ScheduleFragment.this.thirtyDayDue = due;
                binding.fragmentScheduleAmount30.setText(due.getDisplayDue());
                binding.fragmentScheduleService30.setText(due.getDisplayService());
            }
        });

        viewModel.observedFourteenDayDue().removeObservers(getViewLifecycleOwner());
        viewModel.observedFourteenDayDue().observe(getViewLifecycleOwner(), due -> {
            if (due != null) {
                ScheduleFragment.this.fourteenDayDue = due;
                binding.fragmentScheduleAmount14.setText(due.getDisplayDue());
                binding.fragmentScheduleService14.setText(due.getDisplayService());
            }
        });

        viewModel.observedAvailableBenefits().removeObservers(getViewLifecycleOwner());
        viewModel.observedAvailableBenefits().observe(getViewLifecycleOwner(), codes -> {
            if (codes != null) {
                setAvailableBenefits(codes);
            }
        });
    }

    private void displayDueDate(String value) {
        double loan = Double.parseDouble(value);
        viewModel.compute30DaysPayment(loan, benefit);
        viewModel.compute14DaysPayment(loan, benefit);
    }

    private void setAvailableBenefits(ArrayList<Code> available) {

        if (!available.isEmpty()) {
            if (binding.fragmentScheduleBenefitsRoot.getVisibility() == View.GONE)
                binding.fragmentScheduleBenefitsRoot.setVisibility(View.VISIBLE);
        }

        String[] available_benefit_array = new String[available.size()];
        String[] benefit_array = new String[available.size()];

        for (int i = 0; i < available.size(); i++) {
            available_benefit_array[i] = available.get(i).getCode();
            benefit_array[i] = available.get(i).getCode_benefit();
        }

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, available_benefit_array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.fragmentScheduleBenefits.setAdapter(adapter);
        binding.fragmentScheduleBenefits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                benefit = Integer.parseInt(benefit_array[position]);
                displayDueDate(loanForm.getRepayment_loan());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void navigation() {

        binding.fragmentScheduleConfirm.setEnabled(false);
        binding.fragmentScheduleGroup.setOnCheckedChangeListener((group, checkedId) -> {
            int selected = group.getCheckedRadioButtonId();

            RadioButton button = binding.getRoot().findViewById(selected);
            String days = button.getText().toString();

            if (checkedId == binding.fragmentSchedule30Days.getId()) {
                loanForm.setRepayment_days(getDaysToPay(days));
                loanForm.setRepayment_total(String.valueOf(thirtyDayDue.getTotal()));
                loanForm.setRepayment_tax(String.valueOf(thirtyDayDue.getTax()));
                loanForm.setRepayment_interest(String.valueOf(thirtyDayDue.getInterest()));
                loanForm.setRepayment_penalty(String.valueOf(thirtyDayDue.getPenalty()));
                loanForm.setRepayment_interest_used(thirtyDayDue.getInterestUsed());
                binding.fragmentScheduleConfirm.setEnabled(true);
            } else if (checkedId == binding.fragmentSchedule14Days.getId()) {
                loanForm.setRepayment_days(getDaysToPay(days));
                loanForm.setRepayment_total(String.valueOf(fourteenDayDue.getTotal()));
                loanForm.setRepayment_tax(String.valueOf(fourteenDayDue.getTax()));
                loanForm.setRepayment_interest(String.valueOf(fourteenDayDue.getInterest()));
                loanForm.setRepayment_penalty(String.valueOf(fourteenDayDue.getPenalty()));
                loanForm.setRepayment_interest_used(fourteenDayDue.getInterestUsed());
                binding.fragmentScheduleConfirm.setEnabled(true);
            }
        });

        binding.fragmentScheduleConfirm.setOnClickListener(v -> {
            hostable.onEnlist(loanForm);
            hostable.onInflate(v, getString(R.string.tag_fragment_receipt));
        });
    }

    private String getDaysToPay(String value) {
        return value.substring(0, value.indexOf(" "));
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