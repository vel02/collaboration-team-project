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
import cs.collaboration.yescredit.ui.apply.fragment.ApprovedFragmentDirections;
import cs.collaboration.yescredit.ui.apply.fragment.ScheduleFragmentDirections;
import cs.collaboration.yescredit.ui.apply.fragment.eight.AmountFragmentDirections;
import cs.collaboration.yescredit.ui.apply.fragment.five.StepFiveFragmentDirections;
import cs.collaboration.yescredit.ui.apply.fragment.four.StepFourFragmentDirections;
import cs.collaboration.yescredit.ui.apply.fragment.one.StepOneFragmentDirections;
import cs.collaboration.yescredit.ui.apply.fragment.seven.SubmitFragmentDirections;
import cs.collaboration.yescredit.ui.apply.fragment.six.StepSixFragmentDirections;
import cs.collaboration.yescredit.ui.apply.fragment.three.StepThreeFragmentDirections;
import cs.collaboration.yescredit.ui.apply.fragment.two.StepTwoFragmentDirections;
import cs.collaboration.yescredit.ui.apply.model.LoanForm;
import cs.collaboration.yescredit.ui.apply.model.UserForm;
import cs.collaboration.yescredit.viewmodel.ViewModelProviderFactory;

public class ApplyActivity extends BaseActivity implements Hostable {

    private static final String TAG = "ApplyActivity";

    @Override
    public void onEnlist(UserForm userForm) {
        viewModel.setUserForm(userForm);
    }

    @Override
    public void onEnlist(LoanForm LoanForm) {
        viewModel.setLoanForm(LoanForm);
    }

    @Override
    public void onSaveUserInfo(UserForm form) {
        viewModel.saveUserInputForm(form);
    }

    @Override
    public void onInflate(View view, String screen) {
        NavDirections action;
        switch (screen) {
            case "tag_fragment_step_two":
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

            case "tag_fragment_step_six":
                action = StepFiveFragmentDirections.actionStepFiveFragmentToStepSixFragment();
                Navigation.findNavController(view).navigate(action);
                break;

            case "tag_fragment_submit_application":
                action = StepSixFragmentDirections.actionStepSixFragmentToSubmitFragment();
                Navigation.findNavController(view).navigate(action);
                break;

            case "tag_fragment_amount_application":
                action = SubmitFragmentDirections.actionSubmitFragmentToAmountFragment();
                Navigation.findNavController(view).navigate(action);
                break;

            case "tag_fragment_approved":
                action = AmountFragmentDirections.actionAmountFragmentToApprovedFragment();
                Navigation.findNavController(view).navigate(action);
                break;

            case "tag_fragment_schedule":
                action = ApprovedFragmentDirections.actionApprovedFragmentToScheduleFragment();
                Navigation.findNavController(view).navigate(action);
                break;

            case "tag_fragment_receipt":
                action = ScheduleFragmentDirections.actionScheduleFragmentToReceiptFragment();
                Navigation.findNavController(view).navigate(action);
                break;

            default:
                Log.d(TAG, "onInflate: step_one ako nag triggered!");
                break;
        }
    }

    @Inject
    ViewModelProviderFactory providerFactory;

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.resetForm();
    }
}