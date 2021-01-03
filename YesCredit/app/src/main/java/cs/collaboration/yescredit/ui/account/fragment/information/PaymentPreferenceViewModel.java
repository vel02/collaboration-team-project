package cs.collaboration.yescredit.ui.account.fragment.information;

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

import static cs.collaboration.yescredit.util.Keys.DATABASE_FIELD_USER_ID_WITH_UNDERSCORE;
import static cs.collaboration.yescredit.util.Keys.DATABASE_NODE_CARDS;

public class PaymentPreferenceViewModel extends ViewModel {
    private static final String TAG = "PaymentPreferenceViewMo";


    private final DatabaseReference reference;
    private final FirebaseUser currentUser;

    private final MutableLiveData<Card> card;

    @Inject
    public PaymentPreferenceViewModel() {
        this.reference = FirebaseDatabase.getInstance().getReference();
        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();
        this.card = new MutableLiveData<>();
    }

    public void getUserCard() {
        if (currentUser != null) {

            Query query = reference.child(DATABASE_NODE_CARDS)
                    .orderByChild(DATABASE_FIELD_USER_ID_WITH_UNDERSCORE)
                    .equalTo(currentUser.getUid());

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot singleShot : snapshot.getChildren()) {

                        Card current = singleShot.getValue(Card.class);
                        assert current != null;
                        if (current.getCard_status().equals("primary")) {
                            PaymentPreferenceViewModel.this.card.postValue(current);
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

    public LiveData<Card> observedCard() {
        return card;
    }
}
