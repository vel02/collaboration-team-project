package cs.collaboration.yescredit.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.nostra13.universalimageloader.core.ImageLoader;

import javax.inject.Inject;

import cs.collaboration.yescredit.BaseActivity;
import cs.collaboration.yescredit.BuildConfig;
import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.ActivityHomeBinding;
import cs.collaboration.yescredit.ui.account.AccountSettingsActivity;
import cs.collaboration.yescredit.ui.allowable.AllowableActivity;
import cs.collaboration.yescredit.ui.apply.ApplyActivity;
import cs.collaboration.yescredit.ui.existing.ExistingLoanActivity;
import cs.collaboration.yescredit.ui.faq.FaqActivity;
import cs.collaboration.yescredit.ui.payment.PaymentActivity;
import cs.collaboration.yescredit.ui.referral.ReferralActivity;
import cs.collaboration.yescredit.util.UniversalImageLoader;
import cs.collaboration.yescredit.viewmodel.ViewModelProviderFactory;

public class HomeActivity extends BaseActivity {

    private static final String TAG = "HomeActivity";

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    ActivityHomeBinding binding;

    private HomeViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, providerFactory).get(HomeViewModel.class);

        initImageLoader();

        init();
        subscribeObservers();

    }

    private void initImageLoader() {
        UniversalImageLoader imageLoader = new UniversalImageLoader(this);
        ImageLoader.getInstance().init(imageLoader.getConfig());
    }


    private void init() {
        binding.contentHome.homeContentCardLoanStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ExistingLoanActivity.class);
                startActivity(intent);
            }
        });

        binding.contentHome.homeContentCardAllowableLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AllowableActivity.class);
                startActivity(intent);
            }
        });

        binding.contentHome.homeContentCardApplyLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.checkPendingLoan();
            }
        });

        binding.contentHome.homeContentCardReferral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ReferralActivity.class);
                startActivity(intent);
            }
        });

        binding.contentHome.homeContentCardFaqs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, FaqActivity.class);
                startActivity(intent);
            }
        });

    }

    private void subscribeObservers() {
        viewModel.observeIsAllowed().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isNotAllowed) {
                if (isNotAllowed) {
                    Toast.makeText(HomeActivity.this, "You still have On-Going Transaction", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(HomeActivity.this, "You are allowed to make new Transaction", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomeActivity.this, ApplyActivity.class);
                    startActivity(intent);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_account_settings:
                intent = new Intent(this, AccountSettingsActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_account_payment:
                intent = new Intent(this, PaymentActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_account_about:
                AlertDialog basic_reg;
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
                LayoutInflater inflater = ((Activity) this).getLayoutInflater();
                View v = inflater.inflate(R.layout.dialog_about, null);
                TextView name = v.findViewById(R.id.about_version_name);
                name.setText("v" + BuildConfig.VERSION_NAME);
                builder.setView(v);
                builder.setCancelable(true);
                builder.create();
                basic_reg = builder.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}