package cs.collaboration.yescredit.ui.apply;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import cs.collaboration.yescredit.BaseActivity;
import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.ActivityApplyBinding;
import cs.collaboration.yescredit.ui.apply.fragment.StepOneFragment;
import cs.collaboration.yescredit.ui.apply.fragment.StepTwoFragment;
import cs.collaboration.yescredit.ui.apply.model.ApplicationForm;
import cs.collaboration.yescredit.viewmodel.ViewModelProviderFactory;

import static cs.collaboration.yescredit.ui.apply.ApplyViewModel.Screen.STEP_ONE;
import static cs.collaboration.yescredit.ui.apply.ApplyViewModel.Screen.STEP_TWO;

public class ApplyActivity extends BaseActivity implements Hostable {

    private static final String TAG = "ApplyActivity";

    @Override
    public void onFillUp(ApplicationForm applicationForm) {
        sessionManager.setApplicationForm(applicationForm);
        sessionManager.observeApplicationForm().observe(this, form -> {
            if (form != null) {
                Log.d(TAG, "onChanged: form: " + form);
            }
        });

    }

    @Override
    public void onInflate(String screen) {
        switch (screen) {
            case "tag_fragment_step_two":
                //government id
                viewModel.setScreenState(STEP_TWO);
                break;
            default:
                Log.d(TAG, "onInflate: step_one ako nag triggered!");
                break;
        }
    }

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    ActivityApplyBinding binding;

    @Inject
    SessionManager sessionManager;

    private ApplyViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, providerFactory).get(ApplyViewModel.class);
        viewModel.setScreenState(STEP_ONE);
        subscribeObservers();
    }

    private void subscribeObservers() {
        viewModel.observedScreenState().observe(this, screen -> {
            Fragment fragment;
            FragmentTransaction transaction;
            if (screen != null) {
                switch (screen) {
                    case STEP_ONE:
                        fragment = new StepOneFragment();
                        transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(binding.contentApply.contentApplyContainer.getId(), fragment, getString(R.string.tag_fragment_step_one));
                        transaction.commit();
                        break;
                    case STEP_TWO:
                        fragment = new StepTwoFragment();
                        transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(binding.contentApply.contentApplyContainer.getId(), fragment, getString(R.string.tag_fragment_step_two));
                        transaction.commit();
                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAuthenticationState();
    }

    private String tag_fragment;

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
        tag_fragment = fragment.getTag();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag_fragment);
            FragmentTransaction transaction;

            if (fragment != null) {

                assert fragment.getTag() != null;
                if (fragment.getTag().equals(getString(R.string.tag_fragment_step_one))) {
                    super.onBackPressed();
                    return true;

                } else if (fragment.getTag().equals(getString(R.string.tag_fragment_step_two))) {

                    fragment = new StepOneFragment();
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(binding.contentApply.contentApplyContainer.getId(), fragment, getString(R.string.tag_fragment_step_one));
                    transaction.commit();
                    return true;
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

}