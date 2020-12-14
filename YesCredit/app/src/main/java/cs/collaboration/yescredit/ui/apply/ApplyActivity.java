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

import com.google.firebase.database.DatabaseReference;

import javax.inject.Inject;

import cs.collaboration.yescredit.BaseActivity;
import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.ui.apply.fragment.StepOneFragmentDirections;
import cs.collaboration.yescredit.ui.apply.fragment.StepThreeFragmentDirections;
import cs.collaboration.yescredit.ui.apply.fragment.StepTwoFragmentDirections;
import cs.collaboration.yescredit.ui.apply.model.ApplicationForm;
import cs.collaboration.yescredit.viewmodel.ViewModelProviderFactory;

public class ApplyActivity extends BaseActivity implements Hostable {

    private static final String TAG = "ApplyActivity";

    @Override
    public void onEnlist(ApplicationForm applicationForm) {
        sessionManager.setApplicationForm(applicationForm);
    }

    @Override
    public void onSave() {
        sessionManager.observeApplicationForm().observe(this, form -> {
            if (form != null) {
                Log.d(TAG, "onChanged: form: " + form);
//                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//                assert user != null;
//                save(reference, user.getUid(), getString(R.string.database_field_last_name), form.getLast_name());
//                save(reference, user.getUid(), getString(R.string.database_field_first_name), form.getFirst_name());
//                save(reference, user.getUid(), getString(R.string.database_field_middle_name), form.getMiddle_name());
//                save(reference, user.getUid(), getString(R.string.database_field_gender), form.getGender());
//                save(reference, user.getUid(), getString(R.string.database_field_date_of_birth), form.getDate_of_birth());
//                save(reference, user.getUid(), getString(R.string.database_field_government_image), form.getGovernment_id());
//                save(reference, user.getUid(), getString(R.string.database_field_street_address), form.getStreet_address());
//                save(reference, user.getUid(), getString(R.string.database_field_barangay_address), form.getBarangay_address());
//                save(reference, user.getUid(), getString(R.string.database_field_city_address), form.getCity_address());
//                save(reference, user.getUid(), getString(R.string.database_field_province_address), form.getProvince_address());
//                save(reference, user.getUid(), getString(R.string.database_field_postal_address), form.getPostal_address());
            }
        });
    }

    private void save(DatabaseReference reference, String userId, String field, String value) {
        reference.child(getString(R.string.database_node_users))
                .child(userId)
                .child(field).setValue(value);
    }

    @Override
    public void onInflate(View view, String screen) {
        NavDirections action;
        switch (screen) {
            case "tag_fragment_step_two":
                //government id
                action = StepOneFragmentDirections.actionStepOneFragmentToStepTwoFragment();
                Navigation.findNavController(view).navigate(action);
                break;
            case "tag_fragment_step_three":
                action = StepTwoFragmentDirections.actionStepTwoFragmentToStepThreeFragment();
                Navigation.findNavController(view).navigate(action);
                break;
            case "tag_fragment_step_four":
                action = StepThreeFragmentDirections.actionStepThreeFragmentToStepFourFragment();
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