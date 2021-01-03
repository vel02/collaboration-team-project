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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cs.collaboration.yescredit.ui.account.model.Card;
import cs.collaboration.yescredit.util.Keys;

import static cs.collaboration.yescredit.util.Keys.DATABASE_FIELD_USER_ID_WITH_UNDERSCORE;

public class CardAccountViewModel extends ViewModel {

    private static final String TAG = "CardAccountViewModel";

    private final DatabaseReference reference;
    private final FirebaseUser currentUser;

    private final MutableLiveData<List<Card>> cards;

    @Inject
    public CardAccountViewModel() {
        this.reference = FirebaseDatabase.getInstance().getReference();
        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();
        this.cards = new MutableLiveData<>();
    }

    public void getUserCards() {
        if (currentUser != null) {

            Query query = reference.child(Keys.DATABASE_NODE_CARDS)
                    .orderByChild(DATABASE_FIELD_USER_ID_WITH_UNDERSCORE)
                    .equalTo(currentUser.getUid());

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<Card> cards = new ArrayList<>();
                    for (DataSnapshot singleShot : snapshot.getChildren()) {

                        cs.collaboration.yescredit.model.Card current = singleShot.getValue(cs.collaboration.yescredit.model.Card.class);
                        assert current != null;
                        Card card = new Card();
                        card.setId(current.getCard_id());
                        card.setName(current.getCard_name());
                        card.setNumber(current.getCard_number());
                        card.setImage(current.getCard_image());
                        card.setExp_date(current.getCard_expiration());
                        card.setBill_address(addressFormatter(current));
                        card.setStatus(current.getCard_status());
                        cards.add(card);
                    }

                    CardAccountViewModel.this.cards.postValue(cards);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    private String addressFormatter(cs.collaboration.yescredit.model.Card card) {
        return card.getCard_street() + ", " + card.getCard_barangay()
                + "\n" + card.getCard_city() + "\n" + card.getCard_province()
                + "\n" + card.getCard_zipcode() + " " + card.getCard_province().toUpperCase();
    }

    public LiveData<List<Card>> observedCards() {
        return cards;
    }

}
