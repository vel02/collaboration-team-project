package cs.collaboration.yescredit.ui.apply;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import javax.inject.Inject;
import javax.inject.Singleton;

import cs.collaboration.yescredit.ui.apply.model.LoanForm;
import cs.collaboration.yescredit.ui.apply.model.UserForm;

@Singleton
public class SessionManager {

    private static final String TAG = "SessionManager";

    private final MutableLiveData<UserForm> applicationForm = new MutableLiveData<>();
    private final MutableLiveData<LoanForm> loanForm = new MutableLiveData<>();

    @Inject
    public SessionManager() {
        Log.d(TAG, "SessionManager: I am working...");
    }

    public void setApplicationForm(UserForm userForm) {
        if (userForm != null) {
            this.applicationForm.setValue(userForm);
        }
    }

    public void setLoan(LoanForm loanForm) {
        if (loanForm != null) {
            this.loanForm.setValue(loanForm);
        }
    }

    public LiveData<UserForm> observeUserForm() {
        return applicationForm;
    }

    public LiveData<LoanForm> observeLoanForm() {
        return loanForm;
    }
}
