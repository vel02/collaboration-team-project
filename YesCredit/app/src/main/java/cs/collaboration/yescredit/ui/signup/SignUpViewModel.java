package cs.collaboration.yescredit.ui.signup;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

public class SignUpViewModel extends ViewModel {

    private static final String TAG = "SignUpViewModel";

    private final MutableLiveData<State> progressBarState = new MutableLiveData<>();
    private final MutableLiveData<Boolean> redirectToLoginScreen = new MutableLiveData<>();
    private final FirebaseAuth auth;


    @Inject
    public SignUpViewModel() {
        Log.d(TAG, "SignUpViewModel: view model is working...");
        auth = FirebaseAuth.getInstance();
        redirectToLoginScreen.setValue(false);
    }

    /*
        Resource Message
        - Unable to Register
        - Send Verification Email
        - Couldn't Send Verification Email
     */

    public void registerNewEmail(String email, String password) {
        progressBarState.setValue(State.VISIBLE);

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "onComplete: " + task.isSuccessful());
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: uid: " + auth.getCurrentUser().getUid());

                            sendVerificationEmail();
                            auth.signOut();
                            redirectToLoginScreen.setValue(true);

                        } else {
                            Log.d(TAG, "onComplete: Unable to Register");
                        }
                        progressBarState.setValue(State.INVISIBLE);
                    }
                });
    }

    public void sendVerificationEmail() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: Send Verification Email");
                    } else {
                        Log.d(TAG, "onComplete: Couldn't Send Verification Email");
                    }
                }
            });
        }
    }

    public LiveData<State> observeProgressBarState() {
        return progressBarState;
    }

    public LiveData<Boolean> observeDirectToLoginScreen() {
        return redirectToLoginScreen;
    }

    public enum State {VISIBLE, INVISIBLE}

}
