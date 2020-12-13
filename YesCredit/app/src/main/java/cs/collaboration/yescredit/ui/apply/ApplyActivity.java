package cs.collaboration.yescredit.ui.apply;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import cs.collaboration.yescredit.BaseActivity;
import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.ActivityApplyBinding;
import cs.collaboration.yescredit.ui.apply.fragment.StepOneFragment;
import cs.collaboration.yescredit.ui.apply.model.UserInfo;
import cs.collaboration.yescredit.viewmodel.ViewModelProviderFactory;

import static cs.collaboration.yescredit.ui.apply.ApplyViewModel.Screen;
import static cs.collaboration.yescredit.ui.apply.ApplyViewModel.Screen.STEP_ONE;

public class ApplyActivity extends BaseActivity implements Hostable {

    private static final String TAG = "ApplyActivity";

    @Override
    public void onListen(UserInfo userInfo) {

    }

    @Override
    public void onInflate(String screen) {
        switch (screen) {
            case "tag_host":
                Log.d(TAG, "onInflate: step_one ako nag triggered!");
                break;
        }
    }

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    ActivityApplyBinding binding;

    private ApplyViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, providerFactory).get(ApplyViewModel.class);
        viewModel.setScreenState(STEP_ONE);
        subscribeObservers();
    }

    private void subscribeObservers() {
        viewModel.observedScreenState().observe(this, new Observer<Screen>() {
            @Override
            public void onChanged(Screen screen) {
                if (screen != null) {
                    switch (screen) {
                        case STEP_ONE:
                            StepOneFragment fragment = new StepOneFragment();
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(binding.contentApply.contentApplyContainer.getId(), fragment, getString(R.string.tag_fragment_step_one));
                            transaction.commit();
                            break;
                    }
                }
            }
        });
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