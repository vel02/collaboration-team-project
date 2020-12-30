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

    private final MutableLiveData<UserForm> userForm = new MutableLiveData<>();
    private final MutableLiveData<LoanForm> loanForm = new MutableLiveData<>();

    @Inject
    public SessionManager() {
        Log.d(TAG, "SessionManager: I am working...");
        this.userForm.setValue(new UserForm());
        this.loanForm.setValue(new LoanForm());
    }

    public void setUserForm(UserForm userForm) {
        if (userForm != null) {
            this.userForm.setValue(userForm);
        }
    }

    public void setLoanForm(LoanForm loanForm) {
        if (loanForm != null) {
            this.loanForm.setValue(loanForm);
        }
    }

    public LiveData<UserForm> observeUserForm() {
        return userForm;
    }

    public LiveData<LoanForm> observeLoanForm() {
        return loanForm;
    }
}
