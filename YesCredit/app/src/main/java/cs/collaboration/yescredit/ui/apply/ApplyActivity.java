package cs.collaboration.yescredit.ui.apply;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import javax.inject.Inject;

import cs.collaboration.yescredit.BaseActivity;
import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.ui.apply.fragment.StepOneFragmentDirections;
import cs.collaboration.yescredit.ui.apply.model.ApplicationForm;
import cs.collaboration.yescredit.viewmodel.ViewModelProviderFactory;

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
    public void onInflate(View view, String screen) {
        switch (screen) {
            case "tag_fragment_step_two":
                //government id
                NavDirections action = StepOneFragmentDirections.actionStepOneFragmentToStepTwoFragment();
                Navigation.findNavController(view).navigate(action);
                break;
            default:
                Log.d(TAG, "onInflate: step_one ako nag triggered!");
                break;
        }
    }

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    SessionManager sessionManager;

    private ApplyViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);
        viewModel = new ViewModelProvider(this, providerFactory).get(ApplyViewModel.class);

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAuthenticationState();
    }
}