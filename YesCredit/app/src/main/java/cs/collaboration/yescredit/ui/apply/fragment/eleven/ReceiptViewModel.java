package cs.collaboration.yescredit.ui.apply.fragment.eleven;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import cs.collaboration.yescredit.model.Code;
import cs.collaboration.yescredit.model.Loan;
import cs.collaboration.yescredit.ui.apply.SessionManager;
import cs.collaboration.yescredit.ui.apply.model.LoanForm;
import cs.collaboration.yescredit.util.Keys;

import static cs.collaboration.yescredit.ui.apply.fragment.eleven.ReceiptViewModel.EnlistNotification.FAILED;
import static cs.collaboration.yescredit.ui.apply.fragment.eleven.ReceiptViewModel.EnlistNotification.SUCCESS;

public class ReceiptViewModel extends ViewModel {

    private static final String TAG = "ReceiptViewModel";
    private final MutableLiveData<EnlistNotification> enlistNotification;
    private final MediatorLiveData<LoanForm> loanForm;

    private final SessionManager sessionManager;
    private final DatabaseReference reference;
    private final FirebaseUser user;

    @Inject
    public ReceiptViewModel(final SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.reference = FirebaseDatabase.getInstance().getReference();
        this.user = FirebaseAuth.getInstance().getCurrentUser();
        this.loanForm = new MediatorLiveData<>();
        this.enlistNotification = new MutableLiveData<>();
        getLatestUserForm();
    }

    public void enlistUserLoanInformation(LoanForm loanForm) {

        String loanId = reference.child(Keys.DATABASE_NODE_LOAN)
                .push().getKey();

        if (user != null && loanId != null) {
            Loan loan = new Loan();
            loan.setUserId(user.getUid());
            loan.setLoanId(loanId);
            loan.setLevelOfEducation(loanForm.getLevelOfEducation());
            loan.setReason(loanForm.getReason());
            loan.setMoreDetails(loanForm.getMoreDetails());
            loan.setOutstanding(loanForm.getOutstanding());
            loan.setCivilStatus(loanForm.getCivilStatus());
            loan.setSourceOfIncome(loanForm.getSourceOfIncome());
            loan.setIncomePerMonth(loanForm.getIncomePerMonth());
            loan.setLimit(loanForm.getLimit());
            loan.setStatus(loanForm.getStatus());
            loan.setRepayment_loan(loanForm.getRepayment_loan());
            loan.setRepayment_date(getSchedule(getCurrentDate(), Integer.parseInt(loanForm.getRepayment_days())));
            loan.setRepayment_interest(loanForm.getRepayment_interest());
            loan.setRepayment_interest_used(loanForm.getRepayment_interest_used());
            loan.setRepayment_tax(loanForm.getRepayment_tax());
            loan.setRepayment_penalty(loanForm.getRepayment_penalty());
            loan.setRepayment_total(loanForm.getRepayment_total());
            loan.setRepayment_days(loanForm.getRepayment_days());

            reference.child(Keys.DATABASE_NODE_LOAN)
                    .child(loanId).setValue(loan)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            enlistNotification.postValue(SUCCESS);
                        } else {
                            enlistNotification.postValue(FAILED);
                        }
                    });
        }

    }


    public void searchAvailableBenefits() {

        if (user != null) {

            Query query = reference.child(Keys.DATABASE_NODE_CODES)
                    .orderByChild(Keys.DATABASE_FIELD_FROM_ID)
                    .equalTo(user.getUid());

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot singleShot : snapshot.getChildren()) {

                        Code generated = singleShot.getValue(Code.class);
                        if (generated != null) {
                            if (generated.getReferred_status().equals("referred")
                                    && generated.getCode_status().equals("not-used")) {
                                reference.child(Keys.DATABASE_NODE_CODES)
                                        .child(generated.getCodeId())
                                        .child(Keys.DATABASE_FIELD_CODE_STATUS)
                                        .setValue("used");
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

    private void getLatestUserForm() {

        final LiveData<LoanForm> source = sessionManager.observeLoanForm();
        loanForm.addSource(source, loanForm -> {
            assert loanForm != null;
            ReceiptViewModel.this.loanForm.setValue(loanForm);
            ReceiptViewModel.this.loanForm.removeSource(source);
        });
    }

    private String getCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return df.format(c);
    }

    public static String getSchedule(String startDate, int numberOfDays) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(dateFormat.parse(startDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DAY_OF_WEEK, numberOfDays);
        return dateFormat.format(c.getTime());
    }

    public LiveData<LoanForm> observedLoanForm() {
        return loanForm;
    }

    public LiveData<EnlistNotification> observedEnlistNotification() {
        return enlistNotification;
    }

    public enum EnlistNotification {SUCCESS, FAILED}

}
