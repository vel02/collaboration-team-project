package cs.collaboration.yescredit.ui.payment;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
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

import javax.inject.Inject;

import cs.collaboration.yescredit.model.Card;
import cs.collaboration.yescredit.model.Loan;
import cs.collaboration.yescredit.util.Keys;

import static cs.collaboration.yescredit.ui.payment.PaymentViewModel.State.*;

public class PaymentViewModel extends ViewModel {

    private static final String TAG = "PaymentViewModel";

    private final MutableLiveData<Boolean> confirmed;
    private final MutableLiveData<Boolean> complete;
    private final MutableLiveData<Boolean> show;
    private final MutableLiveData<State> statePayment;
    private final MutableLiveData<State> stateCard;
    private final MutableLiveData<Loan> loan;
    private final MutableLiveData<Card> card;
    private final DatabaseReference reference;
    private final FirebaseUser user;


    @Inject
    public PaymentViewModel() {
        confirmed = new MutableLiveData<>();
        complete = new MutableLiveData<>();
        show = new MutableLiveData<>();

        confirmed.setValue(false);
        complete.setValue(false);
        show.setValue(false);

        statePayment = new MutableLiveData<>();
        stateCard = new MutableLiveData<>();
        loan = new MutableLiveData<>();
        card = new MutableLiveData<>();
        reference = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void showAvailableTransaction() {
        statePayment.setValue(NOT_AVAILABLE);
        if (user != null) {

            Query query = reference.child(Keys.DATABASE_NODE_LOAN)
                    .orderByChild(Keys.DATABASE_FIELD_USER_ID)
                    .equalTo(user.getUid());

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot singleShot : snapshot.getChildren()) {

                        Loan loan = singleShot.getValue(Loan.class);
                        assert loan != null;
                        if (loan.getStatus().equals("on-going")) {
                            PaymentViewModel.this.loan.postValue(loan);
                            PaymentViewModel.this.statePayment.postValue(AVAILABLE);
                            return;
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }

    public void getUserPrimaryCard() {
        PaymentViewModel.this.stateCard.postValue(NOT_AVAILABLE);
        if (user != null) {

            Query query = reference.child(Keys.DATABASE_NODE_CARDS)
                    .orderByChild(Keys.DATABASE_FIELD_USER_ID_WITH_UNDERSCORE)
                    .equalTo(user.getUid());

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot singleShot : snapshot.getChildren()) {

                        Card current = singleShot.getValue(Card.class);
                        assert current != null;
                        if (current.getCard_status().equals("primary")) {
                            PaymentViewModel.this.card.postValue(current);
                            PaymentViewModel.this.stateCard.postValue(AVAILABLE);
                            return;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    public void updateUserTransaction() {
        if (user != null) {

            Query query = reference.child(Keys.DATABASE_NODE_LOAN)
                    .orderByChild(Keys.DATABASE_FIELD_USER_ID)
                    .equalTo(user.getUid());

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot singleShot : snapshot.getChildren()) {

                        Loan loan = singleShot.getValue(Loan.class);
                        assert loan != null;
                        if (loan.getStatus().equals("on-going")) {

                            reference.child(Keys.DATABASE_NODE_LOAN)
                                    .child(loan.getLoanId())
                                    .child(Keys.DATABASE_FIELD_LOAN_STATUS)
                                    .setValue("paid");

                            PaymentViewModel.this.complete.postValue(true);
                            return;
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed.setValue(confirmed);
    }

    public void setShow(boolean show) {
        this.show.setValue(show);
    }

    public boolean getConfirmed() {
        return confirmed.getValue();
    }

    public boolean getShow() {
        return show.getValue();
    }

    public LiveData<Boolean> observedComplete() {
        return complete;
    }

    public LiveData<State> observedStatePayment() {
        return statePayment;
    }

    public LiveData<State> observedStateCard() {
        return stateCard;
    }

    public LiveData<Loan> observedLoan() {
        return loan;
    }

    public LiveData<Card> observedCard() {
        return card;
    }

    public enum State {AVAILABLE, NOT_AVAILABLE}
}
