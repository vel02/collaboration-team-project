package cs.collaboration.yescredit.ui.apply.fragment.five;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import cs.collaboration.yescredit.ui.apply.SessionManager;
import cs.collaboration.yescredit.ui.apply.model.LoanForm;

public class StepFiveViewModel extends ViewModel {

    private static final String TAG = "StepFiveViewModel";
    private final SessionManager sessionManager;
    private final MediatorLiveData<LoanForm> loanForm;

    @Inject
    public StepFiveViewModel(final SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.loanForm = new MediatorLiveData<>();
        getLatestUserForm();
    }

    private void getLatestUserForm() {

        final LiveData<LoanForm> source = sessionManager.observeLoanForm();
        loanForm.addSource(source, loanForm -> {
            assert loanForm != null;
            StepFiveViewModel.this.loanForm.setValue(loanForm);
            StepFiveViewModel.this.loanForm.removeSource(source);
        });
    }

    public LiveData<LoanForm> observedLoanForm() {
        return loanForm;
    }

}
