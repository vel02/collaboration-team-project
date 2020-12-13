package cs.collaboration.yescredit.ui.apply;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import cs.collaboration.yescredit.BaseActivity;
import cs.collaboration.yescredit.databinding.ActivityApplyBinding;
import cs.collaboration.yescredit.viewmodel.ViewModelProviderFactory;

public class ApplyActivity extends BaseActivity {

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    ActivityApplyBinding binding;

    private ApplyViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, providerFactory).get(ApplyViewModel.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAuthenticationState();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}