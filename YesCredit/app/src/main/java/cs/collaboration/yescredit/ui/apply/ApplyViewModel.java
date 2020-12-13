package cs.collaboration.yescredit.ui.apply;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

public class ApplyViewModel extends ViewModel {

    private static final String TAG = "ApplyViewModel";

    @Inject
    public ApplyViewModel() {
        Log.d(TAG, "ApplyViewModel: view model is working...");
    }
}
