package cs.collaboration.yescredit.ui.apply.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import javax.inject.Inject;

import cs.collaboration.yescredit.databinding.FragmentScheduleBinding;
import cs.collaboration.yescredit.ui.apply.SessionManager;
import cs.collaboration.yescredit.ui.apply.model.LoanForm;
import dagger.android.support.DaggerFragment;

import static cs.collaboration.yescredit.util.Utility.currencyFormatterWithFixDecimal;

public class ScheduleFragment extends DaggerFragment {

    private static final String TAG = "ScheduleFragment";

    @Inject
    SessionManager sessionManager;

    private FragmentScheduleBinding binding;
    private LoanForm loanForm;

    private double interest30;
    private double interest14;
    private double penalty30;
    private double penalty14;
    private double total30;
    private double total14;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentScheduleBinding.inflate(inflater);
        getLoanInfo();
        navigation();
        return binding.getRoot();
    }

    private void getLoanInfo() {
        sessionManager.observeLoanForm().removeObservers(getViewLifecycleOwner());
        sessionManager.observeLoanForm().observe(getViewLifecycleOwner(), new Observer<LoanForm>() {
            @Override
            public void onChanged(LoanForm loanForm) {
                if (loanForm != null) {
                    Log.d(TAG, "onChanged: " + loanForm.getRepayment_loan());
                    getDueDate(loanForm.getRepayment_loan());
                    ScheduleFragment.this.loanForm = loanForm;
                }
            }
        });
    }


    /*
        lower the interest rate, referral benefits
        max = less 5% for 5+ total referral
        min = less 1% for 1 total referral
     */
    private void getDueDate(String value) {

        double interest_30 = 0.15;
        double interest_14 = 0.12;
        final double tax_30 = 0.008;
        final double tax_14 = 0.006;
        final double penalty = 0.08;

        double loan = Double.parseDouble(value);
        Log.d(TAG, "computeDueDate: loan: " + loan);

        // 30 days to pay
        double service = getServiceFee(loan, interest_30, tax_30);
        double total = getTotal(loan, service);
        total30 = total;
        interest30 = loan * interest_30;
        penalty30 = loan * penalty;

        String display_due = "PHP " + currencyFormatterWithFixDecimal(String.valueOf(total)) + " due (Date)";
        String display_service = "Service Fee = PHP " + currencyFormatterWithFixDecimal(String.valueOf(service)) + "(15% Interest + 0.8% tax)";
        binding.fragmentScheduleAmount30.setText(display_due);
        binding.fragmentScheduleService30.setText(display_service);

        // 14 days to pay
        service = getServiceFee(loan, interest_14, tax_14);
        total = getTotal(loan, service);
        total14 = total;
        interest14 = loan * interest_14;
        penalty14 = loan * penalty;

        display_due = "PHP " + currencyFormatterWithFixDecimal(String.valueOf(total)) + " due (Date)";
        display_service = "Service Fee = PHP " + currencyFormatterWithFixDecimal(String.valueOf(service)) + "(12% Interest + 0.6% tax)";
        binding.fragmentScheduleAmount14.setText(display_due);
        binding.fragmentScheduleService14.setText(display_service);

    }

    private void navigation() {

        binding.fragmentScheduleGroup.setOnCheckedChangeListener((group, checkedId) -> {
            int selected = group.getCheckedRadioButtonId();

            RadioButton button = binding.getRoot().findViewById(selected);
            String days = button.getText().toString();
            loanForm.setRepayment_days(getDaysToPay(days));

            if (checkedId == binding.fragmentSchedule30Days.getId()) {
                loanForm.setRepayment_total(String.valueOf(total30));
                loanForm.setRepayment_interest(String.valueOf(interest30));
                loanForm.setRepayment_penalty(String.valueOf(penalty30));
            } else if (checkedId == binding.fragmentSchedule14Days.getId()) {
                loanForm.setRepayment_total(String.valueOf(total14));
                loanForm.setRepayment_interest(String.valueOf(interest14));
                loanForm.setRepayment_penalty(String.valueOf(penalty14));
            }

            Log.d(TAG, "onCheckedChanged: updated: " + loanForm);
        });

    }

    private String getDaysToPay(String value) {
        return value.substring(0, value.indexOf(" "));
    }

    private double getServiceFee(double loan, double interest, double tax) {
        double due = loan * interest;
        tax = loan * tax;
        return due + tax;
    }

    private double getTotal(double loan, double service) {
        return loan + service;
    }

}