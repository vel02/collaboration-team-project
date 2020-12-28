package cs.collaboration.yescredit.ui.account.fragment.information.preference;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

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

import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.FragmentPaymentMethodBinding;
import cs.collaboration.yescredit.ui.account.Hostable;
import cs.collaboration.yescredit.ui.account.adapter.card.CardSelectedRecyclerAdapter;
import cs.collaboration.yescredit.ui.account.model.Card;
import dagger.android.support.DaggerFragment;

public class PaymentMethodFragment extends DaggerFragment implements CardSelectedRecyclerAdapter.OnCardSelectedRecyclerListener {

    @Override
    public void onCardSelected(Card card) {
        setSelectedCard(card);
    }

    private static final String TAG = "PaymentMethodFragment";

    private FragmentPaymentMethodBinding binding;
    private DatabaseReference reference;
    private FirebaseUser user;
    private Activity activity;

    private CardSelectedRecyclerAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPaymentMethodBinding.inflate(inflater);
        initialization();
        return binding.getRoot();
    }

    private void initialization() {
        reference = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new CardSelectedRecyclerAdapter();
        adapter.setOnCardSelectedRecyclerListener(this);
        binding.recyclerView.setAdapter(adapter);
        getUserCards();
    }

    private void getUserCards() {

        if (user != null) {

            Query query = reference.child(getString(R.string.database_node_cards))
                    .orderByChild(getString(R.string.database_field_user_id_underscore))
                    .equalTo(user.getUid());

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
                        card.setExp_date(current.getCard_expiration());
                        card.setImage(current.getCard_image());
                        card.setStatus(current.getCard_status());
                        card.setBill_address(addressFormatter(current));
                        cards.add(card);
                    }

                    PaymentMethodFragment.this.adapter.refresh(cards);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }

    private void setSelectedCard(Card card) {

        if (user != null) {


            Query query = reference.child(getString(R.string.database_node_cards))
                    .orderByChild(getString(R.string.database_field_user_id_underscore))
                    .equalTo(user.getUid());

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot singleShot : snapshot.getChildren()) {

                        cs.collaboration.yescredit.model.Card current = singleShot.getValue(cs.collaboration.yescredit.model.Card.class);
                        assert current != null;

                        if (current.getCard_status().equals("primary")) {

                            reference.child(getString(R.string.database_node_cards))
                                    .child(current.getCard_id())
                                    .child(getString(R.string.database_field_card_status))
                                    .setValue("not-primary");

                            reference.child(getString(R.string.database_node_cards))
                                    .child(current.getCard_id())
                                    .child(getString(R.string.database_field_card_selected))
                                    .setValue("not-selected");

                            reference.child(getString(R.string.database_node_cards))
                                    .child(card.getId())
                                    .child(getString(R.string.database_field_card_status))
                                    .setValue("primary");

                            reference.child(getString(R.string.database_node_cards))
                                    .child(card.getId())
                                    .child(getString(R.string.database_field_card_selected))
                                    .setValue("selected");

                            getUserCards();
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


    private String addressFormatter(cs.collaboration.yescredit.model.Card card) {
        return card.getCard_street() + ", " + card.getCard_barangay()
                + "\n" + card.getCard_city() + "\n" + card.getCard_province()
                + "\n" + card.getCard_zipcode() + " " + card.getCard_province().toUpperCase();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = getActivity();
        if (!(activity instanceof Hostable)) {
            assert activity != null;
            throw new ClassCastException(activity.getClass().getSimpleName()
                    + " must implement Hostable interface.");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(activity.getResources().getColor(R.color.account_base));
    }

}