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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import javax.inject.Inject;

import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.FragmentCardAccountBinding;
import cs.collaboration.yescredit.ui.account.Hostable;
import cs.collaboration.yescredit.ui.account.adapter.card.CardRecyclerAdapter;
import cs.collaboration.yescredit.ui.account.model.Card;
import cs.collaboration.yescredit.viewmodel.ViewModelProviderFactory;
import dagger.android.support.DaggerFragment;

public class CardAccountFragment extends DaggerFragment implements CardRecyclerAdapter.OnCardRecyclerListener {

    @Override
    public void onCardClick(Card card) {
        Log.d(TAG, "onCardClick: card: " + card);
        NavDirections action = CardAccountFragmentDirections.actionCardAccountFragmentToViewCardFragment(card);
        Navigation.findNavController(CardAccountFragment.this.getView()).navigate(action);
    }

    private static final String TAG = "CardAccountFragment";

    @Inject
    ViewModelProviderFactory providerFactory;

    private FragmentCardAccountBinding binding;
    private CardAccountViewModel viewModel;
    private Hostable hostable;

    private CardRecyclerAdapter adapter;
    private Activity activity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCardAccountBinding.inflate(inflater);
        viewModel = new ViewModelProvider(this, providerFactory).get(CardAccountViewModel.class);
        initialization();
        subscribeObservers();
        navigation();
        return binding.getRoot();
    }

    private void subscribeObservers() {
        viewModel.observedCards().removeObservers(getViewLifecycleOwner());
        viewModel.observedCards().observe(getViewLifecycleOwner(), cards -> {
            if (cards != null) {
                CardAccountFragment.this.adapter.refresh(cards);
            }
        });
    }

    private void initialization() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new CardRecyclerAdapter();
        adapter.setOnCardRecyclerListener(this);
        binding.recyclerView.setAdapter(adapter);
    }

    private void navigation() {
        binding.fragmentCardAddCard.setOnClickListener(v ->
                hostable.onInflate(v, getString(R.string.tag_fragment_add_card)));
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

        viewModel.getUserCards();
    }
}