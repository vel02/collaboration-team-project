package cs.collaboration.yescredit.ui.login;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import static cs.collaboration.yescredit.ui.login.LoginViewModel.Screen.HOME;
import static cs.collaboration.yescredit.ui.login.LoginViewModel.State.INVISIBLE;
import static cs.collaboration.yescredit.ui.login.LoginViewModel.State.VISIBLE;

public class LoginViewModel extends ViewModel {

    private static final String TAG = "LoginViewModel";

    private final MutableLiveData<State> progressBarState = new MutableLiveData<>();
    private final MutableLiveData<Screen> screenState = new MutableLiveData<>();
    private final MutableLiveData<String> notification = new MutableLiveData<>();


    private final FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Inject
    public LoginViewModel() {
        auth = FirebaseAuth.getInstance();
    }

    public void signIn(String email, String password) {
        progressBarState.setValue(VISIBLE);
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> progressBarState.setValue(INVISIBLE))
                .addOnFailureListener(e -> {
                    notification.postValue("Authentication Failed");
                    progressBarState.setValue(INVISIBLE);
                });
    }

    public void authStateListener() {
        authStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {

                if (user.isEmailVerified()) {

                    Log.d(TAG, "onAuthStateChanged: sign_in: " + user.getUid());
                    Log.d(TAG, "onAuthStateChanged: Authenticated with: " + user.getEmail());

                    screenState.setValue(HOME);
                } else {
                    notification.postValue("Check Your Email Inbox for a Verification Link");
                    auth.signOut();
                }

            } else {
                Log.d(TAG, "onAuthStateChanged: sign_out");
            }
        };

    }

    public LiveData<State> observeProgressBarState() {
        return progressBarState;
    }

    public LiveData<Screen> observeScreenState() {
        return screenState;
    }

    public LiveData<String> observeNotification() {
        return notification;
    }

    public void setScreenState(Screen screen) {
        this.screenState.setValue(screen);
    }

    public void registerAuthListener() {
        auth.addAuthStateListener(authStateListener);
    }

    public void unregisterAuthListener() {
        if (authStateListener != null) auth.removeAuthStateListener(authStateListener);

    }

    public enum State {VISIBLE, INVISIBLE}

    public enum Screen {HOME, FORGOT_PASSWORD, SIGN_UP}

}
