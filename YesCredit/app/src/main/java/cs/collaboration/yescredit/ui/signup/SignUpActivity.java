package cs.collaboration.yescredit.ui.signup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import cs.collaboration.yescredit.databinding.ActivitySignUpBinding;
import cs.collaboration.yescredit.ui.login.LoginActivity;
import cs.collaboration.yescredit.viewmodel.ViewModelProviderFactory;
import dagger.android.support.DaggerAppCompatActivity;

import static android.text.TextUtils.isEmpty;

public class SignUpActivity extends DaggerAppCompatActivity {

    private static final String TAG = "SignUpActivity";

    private static final String DOMAIN_NAME = "gmail.com";

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    ActivitySignUpBinding binding;

    private SignUpViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, providerFactory).get(SignUpViewModel.class);
        register();
        subscribeObservers();
    }

    private void register() {
        binding.contentSignup.signupContentRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: attempting to register.");

                if (!isEmpty(binding.contentSignup.signupContentEmail.getText().toString())
                        && !isEmpty(binding.contentSignup.signupContentPassword.getText().toString())
                        && !isEmpty(binding.contentSignup.signupContentConfirmPassword.getText().toString())) {

                    if (isValidDomain(binding.contentSignup.signupContentEmail.getText().toString())) {

                        if (isPasswordMatch(binding.contentSignup.signupContentPassword.getText().toString(),
                                binding.contentSignup.signupContentConfirmPassword.getText().toString())) {

                            viewModel.registerNewEmail(binding.contentSignup.signupContentEmail.getText().toString(),
                                    binding.contentSignup.signupContentPassword.getText().toString());

                        } else {
                            Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(SignUpActivity.this, "Please Register with valid Gmail Account", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(SignUpActivity.this, "You must fill out all the fields", Toast.LENGTH_SHORT).show();
                }

            }
        });

        hideSoftKeyboard();
    }


    private void subscribeObservers() {
        viewModel.observeProgressBarState().observe(this, new Observer<SignUpViewModel.State>() {
            @Override
            public void onChanged(SignUpViewModel.State state) {
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

        viewModel.observeDirectToLoginScreen().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean != null) {
                    if (aBoolean) {
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

    }

    private boolean isValidDomain(String email) {
        Log.d(TAG, "isValidDomain: isValidDomain: verifying email has correct domain: " + email);
        String domain = email.substring(email.indexOf("@") + 1).toLowerCase();
        Log.d(TAG, "isValidDomain: users domain: " + domain);
        return domain.equals(DOMAIN_NAME);
    }

    private boolean isPasswordMatch(String password, String confirm_password) {
        return password.equals(confirm_password);
    }

    private void showDialog() {
        binding.contentSignup.progressBar.setVisibility(View.VISIBLE);
    }

    private void hideDialog() {
        if (binding.contentSignup.progressBar.getVisibility() == View.VISIBLE)
            binding.contentSignup.progressBar.setVisibility(View.INVISIBLE);
    }

    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

}