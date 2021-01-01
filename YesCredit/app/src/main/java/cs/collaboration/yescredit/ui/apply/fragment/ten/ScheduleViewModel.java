package cs.collaboration.yescredit.ui.apply.fragment.ten;

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

import java.util.ArrayList;

import javax.inject.Inject;

import cs.collaboration.yescredit.model.Code;
import cs.collaboration.yescredit.ui.apply.SessionManager;
import cs.collaboration.yescredit.ui.apply.fragment.ten.model.Due;
import cs.collaboration.yescredit.ui.apply.model.LoanForm;
import cs.collaboration.yescredit.util.Keys;

import static cs.collaboration.yescredit.util.Utility.currencyFormatterWithFixDecimal;

public class ScheduleViewModel extends ViewModel {

    private static final String TAG = "ScheduleViewModel";
    private static final double INTEREST_30_DAYS = 0.15;
    private static final double INTEREST_14_DAYS = 0.12;
    private static final double TAX_30_DAYS = 0.008;
    private static final double TAX_14_DAYS = 0.006;
    private static final double PENALTY = 0.08;

    private final MediatorLiveData<LoanForm> loanForm;
    private final MediatorLiveData<Due> thirtyDayDue;
    private final MediatorLiveData<Due> fourteenDayDue;
    private final MediatorLiveData<ArrayList<Code>> availableBenefits;

    private final SessionManager sessionManager;
    private final DatabaseReference reference;
    private final FirebaseUser user;

    @Inject
    public ScheduleViewModel(final SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.reference = FirebaseDatabase.getInstance().getReference();
        this.user = FirebaseAuth.getInstance().getCurrentUser();
        this.loanForm = new MediatorLiveData<>();
        this.thirtyDayDue = new MediatorLiveData<>();
        this.fourteenDayDue = new MediatorLiveData<>();
        this.availableBenefits = new MediatorLiveData<>();
        getLatestUserForm();
        searchAvailableBenefits();
    }

    private void searchAvailableBenefits() {

        if (user != null) {

            Query query = reference.child(Keys.DATABASE_NODE_CODES)
                    .orderByChild(Keys.DATABASE_FIELD_FROM_ID)
                    .equalTo(user.getUid());

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<Code> available = new ArrayList<>();
                    long number_of_codes = 0;
                    for (DataSnapshot singleShot : snapshot.getChildren()) {
                        number_of_codes++;

                        Code generated = singleShot.getValue(Code.class);

                        if (generated != null) {
                            if (generated.getReferred_status().equals("referred")
                                    && generated.getCode_status().equals("not-used")) {
                                available.add(generated);
                                if (number_of_codes == snapshot.getChildrenCount()) {
                                    availableBenefits.postValue(available);
                                }
                            }
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    public void compute30DaysPayment(double givenLoan, int givenBenefit) {
        String interestUsed = String.valueOf((15 - givenBenefit));

        double benefit = Double.parseDouble("0.0" + givenBenefit);
        double interest = (INTEREST_30_DAYS - benefit);
        double penalty = (givenLoan * PENALTY);

        interest = (givenLoan * interest);
        double tax = (givenLoan * TAX_30_DAYS);
        double serviceFee = interest + tax;
        double total = (givenLoan + serviceFee);

        final Due due = new Due(
                benefit, interest, penalty, serviceFee, total, tax,
                "PHP " + currencyFormatterWithFixDecimal(String.valueOf(total)) + " due (Date)",
                "Service Fee = PHP " + currencyFormatterWithFixDecimal(String.valueOf(serviceFee)) + "(" + interestUsed + "% Interest + 0.8% tax)",
                interestUsed, "30_days");
        Log.d(TAG, "compute30DaysPayment: " + due);
        thirtyDayDue.setValue(due);
    }

    public void compute14DaysPayment(double givenLoan, int givenBenefit) {
        String interestUsed = String.valueOf((12 - givenBenefit));

        double benefit = Double.parseDouble("0.0" + givenBenefit);
        double interest = (INTEREST_14_DAYS - benefit);
        double penalty = (givenLoan * PENALTY);

        interest = (givenLoan * interest);
        double tax = (givenLoan * TAX_14_DAYS);
        double serviceFee = interest + tax;
        double total = (givenLoan + serviceFee);

        final Due due = new Due(
                benefit, interest, penalty, serviceFee, total, tax,
                "PHP " + currencyFormatterWithFixDecimal(String.valueOf(total)) + " due (Date)",
                "Service Fee = PHP " + currencyFormatterWithFixDecimal(String.valueOf(serviceFee)) + "(" + interestUsed + "% Interest + 0.6% tax)",
                interestUsed, "14_days");
        Log.d(TAG, "compute14DaysPayment: " + due);
        fourteenDayDue.setValue(due);
    }

    private void getLatestUserForm() {

        final LiveData<LoanForm> source = sessionManager.observeLoanForm();
        loanForm.addSource(source, loanForm -> {
            assert loanForm != null;
            ScheduleViewModel.this.loanForm.setValue(loanForm);
            ScheduleViewModel.this.loanForm.removeSource(source);
        });
    }

    public LiveData<LoanForm> observedLoanForm() {
        return loanForm;
    }

    public LiveData<Due> observedThirtyDayDue() {
        return thirtyDayDue;
    }

    public LiveData<Due> observedFourteenDayDue() {
        return fourteenDayDue;
    }

    public LiveData<ArrayList<Code>> observedAvailableBenefits() {
        return availableBenefits;
    }

}
