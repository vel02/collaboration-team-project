package cs.collaboration.yescredit.ui.existing;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

public class ExistingLoanViewModel extends ViewModel {

    private static final String TAG = "ExistingLoanViewModel";

    @Inject
    public ExistingLoanViewModel() {
        Log.d(TAG, "ExistingLoanViewModel: view model is working...");
    }
}
