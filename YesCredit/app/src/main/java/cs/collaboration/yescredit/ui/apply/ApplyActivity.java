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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;

import cs.collaboration.yescredit.BaseActivity;
import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.ui.apply.fragment.StepFourFragmentDirections;
import cs.collaboration.yescredit.ui.apply.fragment.StepOneFragmentDirections;
import cs.collaboration.yescredit.ui.apply.fragment.StepThreeFragmentDirections;
import cs.collaboration.yescredit.ui.apply.fragment.StepTwoFragmentDirections;
import cs.collaboration.yescredit.ui.apply.model.LoanForm;
import cs.collaboration.yescredit.ui.apply.model.UserForm;
import cs.collaboration.yescredit.viewmodel.ViewModelProviderFactory;

public class ApplyActivity extends BaseActivity implements Hostable {

    private static final String TAG = "ApplyActivity";

    @Override
    public void onEnlist(UserForm userForm) {
        sessionManager.setApplicationForm(userForm);
    }

    @Override
    public void onEnlist(LoanForm LoanForm) {
        sessionManager.setLoan(LoanForm);
    }

    @Override
    public void onSaveUserInfo(UserForm form) {
        if (form != null) {
            Log.d(TAG, "onChanged: form: " + form);
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                assert user != null;
                save(reference, user.getUid(), getString(R.string.database_field_last_name), form.getLast_name() != null ? form.getLast_name() : "");
                save(reference, user.getUid(), getString(R.string.database_field_first_name), form.getFirst_name() != null ? form.getFirst_name() : "");
                save(reference, user.getUid(), getString(R.string.database_field_middle_name), form.getMiddle_name() != null ? form.getMiddle_name() : "");
                save(reference, user.getUid(), getString(R.string.database_field_gender), form.getGender() != null ? form.getGender() : "");
                save(reference, user.getUid(), getString(R.string.database_field_date_of_birth), form.getDate_of_birth() != null ? form.getDate_of_birth() : "");
                save(reference, user.getUid(), getString(R.string.database_field_government_image), form.getGovernment_id() != null ? form.getGovernment_id() : "");
                save(reference, user.getUid(), getString(R.string.database_field_street_address), form.getStreet_address() != null ? form.getStreet_address() : "");
                save(reference, user.getUid(), getString(R.string.database_field_barangay_address), form.getBarangay_address() != null ? form.getBarangay_address() : "");
                save(reference, user.getUid(), getString(R.string.database_field_city_address), form.getCity_address() != null ? form.getCity_address() : "");
                save(reference, user.getUid(), getString(R.string.database_field_province_address), form.getProvince_address() != null ? form.getProvince_address() : "");
                save(reference, user.getUid(), getString(R.string.database_field_postal_address), form.getPostal_address() != null ? form.getPostal_address() : "");
        }
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

            case "tag_fragment_personal_info":
                action = StepFourFragmentDirections.actionStepFourFragmentToPersonalInfoFragment();
                Navigation.findNavController(view).navigate(action);
                break;

            case "tag_fragment_address":
                action = StepFourFragmentDirections.actionStepFourFragmentToAddressFragment();
                Navigation.findNavController(view).navigate(action);
                break;

            case "tag_fragment_step_five":
                action = StepFourFragmentDirections.actionStepFourFragmentToStepFiveFragment();
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

    private void save(DatabaseReference reference, String userId, String field, String value) {
        reference.child(getString(R.string.database_node_users))
                .child(userId)
                .child(field).setValue(value);
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