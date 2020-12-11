package cs.collaboration.yescredit.ui.referral;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

public class ReferralViewModel extends ViewModel {

    private static final String TAG = "ReferralViewModel";

    @Inject
    public ReferralViewModel() {
        Log.d(TAG, "ReferralViewModel: view model is working...");
    }
}
