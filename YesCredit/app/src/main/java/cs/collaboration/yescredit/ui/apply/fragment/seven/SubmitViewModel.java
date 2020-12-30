package cs.collaboration.yescredit.ui.apply.fragment.seven;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import javax.inject.Inject;

import cs.collaboration.yescredit.model.Loan;
import cs.collaboration.yescredit.ui.apply.SessionManager;
import cs.collaboration.yescredit.ui.apply.model.LoanForm;

import static cs.collaboration.yescredit.util.Keys.DATABASE_FIELD_USER_ID;
import static cs.collaboration.yescredit.util.Keys.DATABASE_NODE_LOAN;

public class SubmitViewModel extends ViewModel {

    private static final String TAG = "SubmitViewModel";
    private final SessionManager sessionManager;
    private final DatabaseReference reference;
    private final FirebaseUser user;
    private final MediatorLiveData<LoanForm> loanForm;

    private long numberOfPaidLoan;

    @Inject
    public SubmitViewModel(final SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.reference = FirebaseDatabase.getInstance().getReference();
        this.user = FirebaseAuth.getInstance().getCurrentUser();
        this.loanForm = new MediatorLiveData<>();
        getLatestUserForm();
        getNumberOfPaidLoan();
    }

    private void getNumberOfPaidLoan() {
        Query query = reference.child(DATABASE_NODE_LOAN)
                .orderByChild(DATABASE_FIELD_USER_ID)
                .equalTo(user.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot singleShot : snapshot.getChildren()) {
                    Loan loan = singleShot.getValue(Loan.class);
                    if (loan != null) {
                        String status = loan.getStatus();
                        if (status.equals("paid")) {
                            numberOfPaidLoan++;
                            Log.d(TAG, "onDataChange: " + numberOfPaidLoan);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getLatestUserForm() {

        final LiveData<LoanForm> source = sessionManager.observeLoanForm();
        loanForm.addSource(source, loanForm -> {
            assert loanForm != null;
            SubmitViewModel.this.loanForm.setValue(loanForm);
            SubmitViewModel.this.loanForm.removeSource(source);
        });
    }

    public long getNumberOfPaid() {
        return numberOfPaidLoan;
    }

    public void setNumberOfPaid(long numberOfPaidLoan) {
        this.numberOfPaidLoan = numberOfPaidLoan;
    }

    public LiveData<LoanForm> observedLoanForm() {
        return loanForm;
    }

}
