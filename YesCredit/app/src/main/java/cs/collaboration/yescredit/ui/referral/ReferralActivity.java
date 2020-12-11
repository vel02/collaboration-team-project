package cs.collaboration.yescredit.ui.referral;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import cs.collaboration.yescredit.BaseActivity;
import cs.collaboration.yescredit.databinding.ActivityReferralBinding;
import cs.collaboration.yescredit.viewmodel.ViewModelProviderFactory;

public class ReferralActivity extends BaseActivity {

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    ActivityReferralBinding binding;

    private ReferralViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, providerFactory).get(ReferralViewModel.class);

    }
}