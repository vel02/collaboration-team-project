package cs.collaboration.yescredit.ui.apply.fragment.eight;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import cs.collaboration.yescredit.ui.apply.SessionManager;
import cs.collaboration.yescredit.ui.apply.model.LoanForm;

public class AmountViewModel extends ViewModel {

    private static final String TAG = "AmountViewModel";
    private final SessionManager sessionManager;
    private final MediatorLiveData<LoanForm> loanForm;

    @Inject
    public AmountViewModel(final SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.loanForm = new MediatorLiveData<>();
        getLatestUserForm();
    }

    private void getLatestUserForm() {

        final LiveData<LoanForm> source = sessionManager.observeLoanForm();
        loanForm.addSource(source, loanForm -> {
            assert loanForm != null;
            AmountViewModel.this.loanForm.setValue(loanForm);
            AmountViewModel.this.loanForm.removeSource(source);
        });
    }

    public LiveData<LoanForm> observedLoanForm() {
        return loanForm;
    }

}
