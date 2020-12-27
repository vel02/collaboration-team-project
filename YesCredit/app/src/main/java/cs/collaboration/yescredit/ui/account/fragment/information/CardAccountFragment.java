package cs.collaboration.yescredit.ui.account.fragment.information;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
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
import cs.collaboration.yescredit.databinding.FragmentCardAccountBinding;
import cs.collaboration.yescredit.ui.account.Hostable;
import cs.collaboration.yescredit.ui.account.adapter.card.CardRecyclerAdapter;
import cs.collaboration.yescredit.ui.account.model.Card;
import dagger.android.support.DaggerFragment;

public class CardAccountFragment extends DaggerFragment implements CardRecyclerAdapter.OnCardRecyclerListener {

    @Override
    public void onCardClick(Card card) {
        Log.d(TAG, "onCardClick: card: " + card);
        NavDirections action = CardAccountFragmentDirections.actionCardAccountFragmentToViewCardFragment(card);
        Navigation.findNavController(CardAccountFragment.this.getView()).navigate(action);
    }

    private static final String TAG = "CardAccountFragment";

    private FragmentCardAccountBinding binding;
    private DatabaseReference reference;
    private Hostable hostable;

    private CardRecyclerAdapter adapter;
    private Activity activity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCardAccountBinding.inflate(inflater);
        initialization();
        navigation();
        return binding.getRoot();
    }

    private void initialization() {
        reference = FirebaseDatabase.getInstance().getReference();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new CardRecyclerAdapter();
        adapter.setOnCardRecyclerListener(this);
        binding.recyclerView.setAdapter(adapter);
    }

    private void navigation() {
        binding.fragmentCardAddCard.setOnClickListener(v -> {
            hostable.onInflate(v, getString(R.string.tag_fragment_add_card));
        });
    }

    private void getUserCards() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

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
                        card.setImage(current.getCard_image());
                        card.setExp_date(current.getCard_expiration());
                        card.setBill_address(addressFormatter(current));
                        card.setStatus(current.getCard_status());
                        cards.add(card);
                    }

                    CardAccountFragment.this.adapter.refresh(cards);
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
        assert activity != null;
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(activity.getResources().getColor(R.color.account_base));
        if (!(activity instanceof Hostable)) {
            assert activity != null;
            throw new ClassCastException(activity.getClass().getSimpleName()
                    + " must implement Hostable interface.");
        }
        hostable = (Hostable) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        hostable = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(activity.getResources().getColor(R.color.account_base));

        getUserCards();
    }
}