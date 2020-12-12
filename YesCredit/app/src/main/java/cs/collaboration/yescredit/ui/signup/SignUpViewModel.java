package cs.collaboration.yescredit.ui.signup;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;

import cs.collaboration.yescredit.model.User;
import cs.collaboration.yescredit.util.Keys;

public class SignUpViewModel extends ViewModel {

    private static final String TAG = "SignUpViewModel";

    private final MutableLiveData<State> progressBarState = new MutableLiveData<>();
    private final MutableLiveData<Boolean> redirectToLoginScreen = new MutableLiveData<>();
    private final FirebaseAuth auth;
    private final FirebaseDatabase database;

    @Inject
    public SignUpViewModel() {
        Log.d(TAG, "SignUpViewModel: view model is working...");
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
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
                            createNewUserStorage(email);

                        } else {
                            Log.d(TAG, "onComplete: Unable to Register");
                        }
                        progressBarState.setValue(State.INVISIBLE);
                    }
                });
    }

    public void createNewUserStorage(String email) {
        FirebaseUser current = auth.getCurrentUser();
        if (current == null) return;
        User user = new User();
        user.setName(email.substring(0, email.indexOf("@")));
        user.setPhone("1");
        user.setProfile_image("");
        user.setUser_id(current.getUid());

        database.getReference()
                .child(Keys.DATABASE_NODE_USER)
                .child(current.getUid())
                .setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        auth.signOut();
                        redirectToLoginScreen.setValue(true);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                auth.signOut();
                redirectToLoginScreen.setValue(true);
                Log.d(TAG, "onFailure: something went wrong");
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
