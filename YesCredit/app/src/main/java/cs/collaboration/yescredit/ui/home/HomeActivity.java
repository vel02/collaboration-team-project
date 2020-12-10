package cs.collaboration.yescredit.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import cs.collaboration.yescredit.BaseActivity;
import cs.collaboration.yescredit.databinding.ActivityHomeBinding;
import cs.collaboration.yescredit.ui.faq.FaqActivity;
import cs.collaboration.yescredit.viewmodel.ViewModelProviderFactory;

public class HomeActivity extends BaseActivity {

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    ActivityHomeBinding binding;

    private HomeViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, providerFactory).get(HomeViewModel.class);

        init();

    }

    private void init() {
        binding.contentHome.homeContentCardLoanStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "Existing Loan Status", Toast.LENGTH_SHORT).show();
            }
        });

        binding.contentHome.homeContentCardAllowableLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "Allowable Loan", Toast.LENGTH_SHORT).show();
            }
        });

        binding.contentHome.homeContentCardApplyLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "Apply Loan", Toast.LENGTH_SHORT).show();
            }
        });

        binding.contentHome.homeContentCardReferral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "Referral", Toast.LENGTH_SHORT).show();
            }
        });

        binding.contentHome.homeContentCardFaqs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "FAQs", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomeActivity.this, FaqActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAuthenticationState();
    }
}