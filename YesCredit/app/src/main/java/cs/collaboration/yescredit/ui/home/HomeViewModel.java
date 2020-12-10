package cs.collaboration.yescredit.ui.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

public class HomeViewModel extends ViewModel {

    private static final String TAG = "HomeViewModel";

    private MutableLiveData<Screen> screenState = new MutableLiveData<>();

    @Inject
    public HomeViewModel() {
        Log.d(TAG, "HomeViewModel: view model is working...");
    }



    public LiveData<Screen> observeScreenState() {
        return screenState;
    }

    public void setScreenState(Screen screen) {
        this.screenState.setValue(screen);
    }

    public enum Screen {EXISTING_LOAN_STATUS, ALLOWABLE_LOAN, APPLY_LOAN, REFERRAL, FAQS}

}
