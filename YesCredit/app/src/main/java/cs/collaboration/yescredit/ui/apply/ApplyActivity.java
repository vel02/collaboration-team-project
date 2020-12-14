package cs.collaboration.yescredit.ui.apply;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

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
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);
        viewModel = new ViewModelProvider(this, providerFactory).get(ApplyViewModel.class);
        navigationController();

    }

    private void navigationController() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationUI.setupWithNavController(toolbar, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAuthenticationState();
    }
}