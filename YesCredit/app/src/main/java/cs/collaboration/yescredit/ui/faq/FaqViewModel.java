package cs.collaboration.yescredit.ui.faq;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

public class FaqViewModel extends ViewModel {

    private static final String TAG = "FaqViewModel";

    @Inject
    public FaqViewModel() {
        Log.d(TAG, "FaqViewModel: view model is working...");
    }
}
