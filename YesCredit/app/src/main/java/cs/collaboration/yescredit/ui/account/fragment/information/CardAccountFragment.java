package cs.collaboration.yescredit.ui.account.fragment.information;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.FragmentCardAccountBinding;
import cs.collaboration.yescredit.ui.account.Hostable;
import cs.collaboration.yescredit.ui.account.adapter.CardRecyclerAdapter;
import cs.collaboration.yescredit.ui.account.model.Card;
import dagger.android.support.DaggerFragment;

public class CardAccountFragment extends DaggerFragment implements CardRecyclerAdapter.OnCardRecyclerListener {

    @Override
    public void onCardClick(Card card) {
        Log.d(TAG, "onCardClick: card: " + card);
        Toast.makeText(requireActivity(), "card: " + card, Toast.LENGTH_SHORT).show();
    }

    private static final String TAG = "CardAccountFragment";

    private FragmentCardAccountBinding binding;
    private Hostable hostable;

    private CardRecyclerAdapter adapter;
    private Activity activity;

    //tester
    private final List<Card> cards = new ArrayList<>();
    private int count;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCardAccountBinding.inflate(inflater);
        initialization();
        navigation();
        return binding.getRoot();
    }

    private void initialization() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new CardRecyclerAdapter();
        adapter.setOnCardRecyclerListener(this);
        binding.recyclerView.setAdapter(adapter);
    }

    private void navigation() {
        binding.fragmentCardAddCard.setOnClickListener(v -> {
            hostable.onInflate(v, getString(R.string.tag_fragment_add_card));
//            Card card = new Card();
//            card.setName("American Express");
//            card.setNumber("6542-5645-9879-6543");
//            card.setExp_date("05/26");
//            card.setImage("ic_american_express");
//            card.setBill_address("195 - y 25th Avenue, East Rembo\nMakati City\nMetro Manila Philippines\n1219 METRO MANILA");
//            cards.add(card);
//            adapter.refresh(cards);
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = getActivity();
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
    }
}