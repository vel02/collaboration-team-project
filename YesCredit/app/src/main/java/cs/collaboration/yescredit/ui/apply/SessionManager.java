package cs.collaboration.yescredit.ui.apply;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import javax.inject.Inject;
import javax.inject.Singleton;

import cs.collaboration.yescredit.ui.apply.model.ApplicationForm;

@Singleton
public class SessionManager {

    private static final String TAG = "SessionManager";

    private final MutableLiveData<ApplicationForm> applicationForm = new MediatorLiveData<>();

    @Inject
    public SessionManager() {
        Log.d(TAG, "SessionManager: I am working...");
    }

    public void setApplicationForm(ApplicationForm applicationForm) {
        if (applicationForm != null) {
            this.applicationForm.setValue(applicationForm);
        }
    }

    public LiveData<ApplicationForm> observeApplicationForm() {
        return applicationForm;
    }
}
