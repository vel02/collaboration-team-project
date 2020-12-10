package cs.collaboration.yescredit.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import cs.collaboration.yescredit.databinding.ActivityLoginBinding;
import cs.collaboration.yescredit.ui.home.HomeActivity;
import cs.collaboration.yescredit.ui.login.dialog.PasswordResetDialog;
import cs.collaboration.yescredit.ui.signup.SignUpActivity;
import cs.collaboration.yescredit.viewmodel.ViewModelProviderFactory;
import dagger.android.support.DaggerAppCompatActivity;

import static android.text.TextUtils.isEmpty;
import static cs.collaboration.yescredit.ui.login.LoginViewModel.Screen;
import static cs.collaboration.yescredit.ui.login.LoginViewModel.Screen.FORGOT_PASSWORD;
import static cs.collaboration.yescredit.ui.login.LoginViewModel.Screen.SIGN_UP;
import static cs.collaboration.yescredit.ui.login.LoginViewModel.State;
import static cs.collaboration.yescredit.util.Utility.hideSoftKeyboard;

public class LoginActivity extends DaggerAppCompatActivity {

    private static final String TAG = "LoginActivity";

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    ActivityLoginBinding binding;

    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, providerFactory).get(LoginViewModel.class);
        viewModel.authStateListener();

        init();

        subscribeObservers();
        hideSoftKeyboard(this);
    }

    private void init() {
        binding.contentLogin.loginContentLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isEmpty(binding.contentLogin.loginContentEmail.getText().toString())
                        && !isEmpty(binding.contentLogin.loginContentPassword.getText().toString())) {

                    Log.d(TAG, "onClick: attempting to authenticate");

                    viewModel.signIn(binding.contentLogin.loginContentEmail.getText().toString(),
                            binding.contentLogin.loginContentPassword.getText().toString());

                } else {
                    Toast.makeText(LoginActivity.this, "You didn't fill in all the fields", Toast.LENGTH_SHORT).show();
                }


            }
        });

        binding.contentLogin.loginContentSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.setScreenState(SIGN_UP);
            }
        });

        binding.contentLogin.loginContentForgotPassword.setOnClickListener(v -> {
            viewModel.setScreenState(FORGOT_PASSWORD);
        });
    }

    private void subscribeObservers() {
        viewModel.observeScreenState().observe(this, new Observer<Screen>() {
            @Override
            public void onChanged(Screen screen) {
                Intent intent;
                if (screen != null) {
                    switch (screen) {
                        case HOME:
                            intent = new Intent(LoginActivity.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                            break;
                        case SIGN_UP:
                            intent = new Intent(LoginActivity.this, SignUpActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            break;
                        case FORGOT_PASSWORD:
                            PasswordResetDialog dialog = new PasswordResetDialog();
                            dialog.show(getSupportFragmentManager(), "password_reset_dialog");
                            break;
                    }
                }
            }
        });

        viewModel.observeProgressBarState().observe(this, new Observer<State>() {
            @Override
            public void onChanged(State state) {
                if (state != null) {
                    switch (state) {
                        case VISIBLE:
                            showDialog();
                            break;
                        case INVISIBLE:
                            hideDialog();
                            break;
                    }
                }
            }
        });
    }

    private void showDialog() {
        binding.contentLogin.progressBar.setVisibility(View.VISIBLE);
    }

    private void hideDialog() {
        if (binding.contentLogin.progressBar.getVisibility() == View.VISIBLE)
            binding.contentLogin.progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.registerAuthListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        viewModel.unregisterAuthListener();
    }
}