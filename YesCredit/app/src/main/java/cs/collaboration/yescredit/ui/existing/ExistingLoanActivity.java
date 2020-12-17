package cs.collaboration.yescredit.ui.existing;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import cs.collaboration.yescredit.BaseActivity;
import cs.collaboration.yescredit.databinding.ActivityExistingLoanBinding;
import cs.collaboration.yescredit.viewmodel.ViewModelProviderFactory;

public class ExistingLoanActivity extends BaseActivity {

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    ActivityExistingLoanBinding binding;

    private ExistingLoanViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, providerFactory).get(ExistingLoanViewModel.class);

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAuthenticationState();
    }
}