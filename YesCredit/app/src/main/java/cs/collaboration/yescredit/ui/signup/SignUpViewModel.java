package cs.collaboration.yescredit.ui.signup;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

public class SignUpViewModel extends ViewModel {

    private static final String TAG = "SignUpViewModel";

    @Inject
    public SignUpViewModel() {
        Log.d(TAG, "SignUpViewModel: view model is working...");
    }
}
