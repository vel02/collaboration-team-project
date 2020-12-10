package cs.collaboration.yescredit.ui.login.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;

import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.DialogPasswordResetBinding;
import dagger.android.support.DaggerDialogFragment;

import static android.text.TextUtils.isEmpty;
import static cs.collaboration.yescredit.util.Utility.hideSoftKeyboard;

public class PasswordResetDialog extends DaggerDialogFragment {

    private static final String TAG = "PasswordResetDialog";

    private static final String VALID_DOMAIN = "gmail.com";
    DialogPasswordResetBinding binding;

    private String email;

    @Override
    public int getTheme() {
        return R.style.RoundedCornersDialog;
    }

    private void getEmail() {
        email = binding.dialogForgotPassEmail.getText().toString();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DialogPasswordResetBinding.inflate(inflater);
        confirm();
        return binding.getRoot();
    }

    private void confirm() {
        binding.dialogForgotPassConfirm.setOnClickListener(view -> {
            getEmail();
            if (!isEmpty(email)) {
                if (isValid(email)) {
                    resetPassword(email);
                } else
                    Toast.makeText(getActivity(), "Invalid email. Please use a valid domain", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(getActivity(), "You must fill out the field", Toast.LENGTH_SHORT).show();
            hideSoftKeyboard(getActivity());
        });
    }

    private void resetPassword(String email) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Password Reset Link Sent", Toast.LENGTH_SHORT).show();
                        getDialog().dismiss();
                    } else {
                        Toast.makeText(getActivity(), "No User is Associated with that Email", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static boolean isValid(String email) {
        return email.substring(email.indexOf('@') + 1).equals(VALID_DOMAIN);
    }
}