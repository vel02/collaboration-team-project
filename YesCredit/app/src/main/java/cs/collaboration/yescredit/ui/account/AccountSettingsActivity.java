package cs.collaboration.yescredit.ui.account;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.auth.FirebaseAuth;

import cs.collaboration.yescredit.BaseActivity;
import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.ui.account.fragment.InformationFragmentDirections;
import cs.collaboration.yescredit.ui.account.fragment.information.CardAccountFragmentDirections;
import cs.collaboration.yescredit.ui.account.fragment.information.PaymentPreferenceFragmentDirections;
import cs.collaboration.yescredit.ui.account.fragment.information.PersonalFragmentDirections;
import cs.collaboration.yescredit.ui.account.fragment.information.card.AddCardFragmentDirections;
import cs.collaboration.yescredit.ui.account.fragment.information.card.BillingAddressFragmentDirections;
import cs.collaboration.yescredit.ui.account.fragment.information.personal.AddressesFragmentDirections;

public class AccountSettingsActivity extends BaseActivity implements Hostable {

    @Override
    public void onLogout() {
        FirebaseAuth.getInstance().signOut();
        checkAuthenticationState();
    }

    @Override
    public void onInflate(View view, String screen) {
        NavDirections action;

        switch (screen) {
            case "tag_fragment_personal":
                action = InformationFragmentDirections.actionPersonalInformationFragmentToPersonalFragment();
                Navigation.findNavController(view).navigate(action);
                break;

            case "tag_fragment_card":
                action = InformationFragmentDirections.actionPersonalInformationFragmentToCardAccountFragment();
                Navigation.findNavController(view).navigate(action);
                break;

            case "tag_fragment_add_card":
                action = CardAccountFragmentDirections.actionCardAccountFragmentToAddCardFragment();
                Navigation.findNavController(view).navigate(action);
                break;

            case "tag_fragment_billing_address":
                action = AddCardFragmentDirections.actionAddCardFragmentToBillingAddressFragment();
                Navigation.findNavController(view).navigate(action);
                break;

            case "tag_fragment_add_billing_address":
                action = BillingAddressFragmentDirections.actionBillingAddressFragmentToAddBillingFragment();
                Navigation.findNavController(view).navigate(action);
                break;

            case "tag_fragment_preference":
                action = InformationFragmentDirections.actionPersonalInformationFragmentToPaymentPreferenceFragment();
                Navigation.findNavController(view).navigate(action);
                break;

            case "tag_fragment_phone_number":
                action = PersonalFragmentDirections.actionPersonalFragmentToPhoneNumberFragment();
                Navigation.findNavController(view).navigate(action);
                break;

            case "tag_fragment_addresses":
                action = PersonalFragmentDirections.actionPersonalFragmentToAddressesFragment();
                Navigation.findNavController(view).navigate(action);
                break;

            case "tag_fragment_add_address":
                action = AddressesFragmentDirections.actionAddressesFragmentToAddAddressFragment();
                Navigation.findNavController(view).navigate(action);
                break;

            case "tag_fragment_payment_preference":
                action = PaymentPreferenceFragmentDirections.actionPaymentPreferenceFragmentToPaymentMethodFragment();
                Navigation.findNavController(view).navigate(action);
                break;


        }

    }

    private static final String TAG = "AccountSettingsActivity";

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        navigationController();
        navigation();
    }

    private void getActionPayment() {

        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            boolean isCalled = intent.getBoolean("add_credit_card", false);
            if (isCalled) {
                NavDirections action = InformationFragmentDirections.actionPersonalInformationFragmentToAddCardFragment();
                navController.navigate(action);
            }
        }

    }

    private void navigationController() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationUI.setupWithNavController(toolbar, navController);
    }

    private void navigation() {

    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAuthenticationState();
        getActionPayment();
    }

}