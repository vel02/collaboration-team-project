package cs.collaboration.yescredit.ui.home;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

public class HomeViewModel extends ViewModel {

    private static final String TAG = "HomeViewModel";

    @Inject
    public HomeViewModel() {
        Log.d(TAG, "HomeViewModel: view model is working...");
    }
}
