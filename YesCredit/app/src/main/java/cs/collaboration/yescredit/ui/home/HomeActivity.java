package cs.collaboration.yescredit.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.nostra13.universalimageloader.core.ImageLoader;

import javax.inject.Inject;

import cs.collaboration.yescredit.BaseActivity;
import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.ActivityHomeBinding;
import cs.collaboration.yescredit.ui.allowable.AllowableActivity;
import cs.collaboration.yescredit.ui.apply.ApplyActivity;
import cs.collaboration.yescredit.ui.faq.FaqActivity;
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
                Toast.makeText(HomeActivity.this, "Existing Loan Status", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_sign_out) {
            FirebaseAuth.getInstance().signOut();
            checkAuthenticationState();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}