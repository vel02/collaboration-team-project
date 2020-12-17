package cs.collaboration.yescredit.ui.existing;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import cs.collaboration.yescredit.BaseActivity;
import cs.collaboration.yescredit.databinding.ActivityExistingLoanBinding;
import cs.collaboration.yescredit.ui.allowable.AllowableActivity;
import cs.collaboration.yescredit.ui.apply.ApplyActivity;
import cs.collaboration.yescredit.viewmodel.ViewModelProviderFactory;

import static cs.collaboration.yescredit.util.Utility.currencyFormatter;

public class ExistingLoanActivity extends BaseActivity {

    private static final String TAG = "ExistingLoanActivity";

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    ActivityExistingLoanBinding binding;

    private ExistingLoanViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, providerFactory).get(ExistingLoanViewModel.class);
        navigation();
        subscribeObservers();
        viewModel.getOnGoingLoan();

    }

    private void subscribeObservers() {
        viewModel.observerLoan().observe(this, loan -> {
            if (loan != null) {
                Log.d(TAG, "onChanged: " + loan);
                binding.contentExistingLoan.contentExistingExistingRoot.setVisibility(View.VISIBLE);
                binding.contentExistingLoan.contentExistingApplyRoot.setVisibility(View.GONE);

                String repayment = "PHP " + currencyFormatter(loan.getRepayment_loan());
                String schedule = loan.getRepayment_days() + " Days to Pay";
                String interest = "PHP " + currencyFormatter(loan.getRepayment_interest());
                String total = "PHP " + currencyFormatter(loan.getRepayment_total());

                binding.contentExistingLoan.contentExistingRepayment.setText(repayment);
                binding.contentExistingLoan.contentExistingDaysToPay.setText(schedule);
                binding.contentExistingLoan.contentExistingDate.setText(loan.getRepayment_date());
                binding.contentExistingLoan.contentExistingLoan.setText(repayment);
                binding.contentExistingLoan.contentExistingInterest.setText(interest);
                binding.contentExistingLoan.contentExistingTotal.setText(total);
            } else {
                binding.contentExistingLoan.contentExistingExistingRoot.setVisibility(View.GONE);
                binding.contentExistingLoan.contentExistingApplyRoot.setVisibility(View.VISIBLE);
                String repayment = "PHP 0.00";
                binding.contentExistingLoan.contentExistingRepayment.setText(repayment);
            }
        });

        viewModel.observeIsAllowed().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isNotAllowed) {
                if (isNotAllowed) {
                    Toast.makeText(ExistingLoanActivity.this, "You still have On-Going Transaction", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ExistingLoanActivity.this, "You are allowed to make new Transaction", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ExistingLoanActivity.this, ApplyActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void navigation() {
        binding.contentExistingLoan.contentExistingAllowable.setOnClickListener(v -> {
            Intent intent = new Intent(ExistingLoanActivity.this, AllowableActivity.class);
            startActivity(intent);
            finish();
        });

        binding.contentExistingLoan.contentExistingApply.setOnClickListener(v -> {
            viewModel.checkPendingLoan();
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkAuthenticationState();
    }
}