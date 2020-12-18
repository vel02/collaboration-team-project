package cs.collaboration.yescredit.ui.account;

import android.os.Bundle;

import cs.collaboration.yescredit.BaseActivity;
import cs.collaboration.yescredit.R;

public class AccountSettingsActivity extends BaseActivity {

    private static final String TAG = "AccountSettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
    }
}