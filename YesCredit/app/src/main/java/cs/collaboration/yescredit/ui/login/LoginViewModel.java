package cs.collaboration.yescredit.ui.login;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

public class LoginViewModel extends ViewModel {

    private static final String TAG = "LoginViewModel";

    @Inject
    public LoginViewModel() {
        Log.d(TAG, "LoginViewModel: view model is working...");
    }
}
