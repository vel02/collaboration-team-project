package cs.collaboration.yescredit.ui.apply;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

public class ApplyViewModel extends ViewModel {

    private static final String TAG = "ApplyViewModel";

    private MutableLiveData<Screen> screenState = new MutableLiveData<>();

    @Inject
    public ApplyViewModel() {
        Log.d(TAG, "ApplyViewModel: view model is working...");
    }

    public LiveData<Screen> observedScreenState() {
        return screenState;
    }

    public void setScreenState(Screen screen) {
        this.screenState.setValue(screen);
    }

    public enum Screen {
        STEP_ONE, STEP_TWO, STEP_THREE, STEP_FOUR, STEP_FIVE,
        STEP_SIX, STEP_SEVEN, STEP_EIGHT, STEP_NINE, STEP_TEN
    }
}
