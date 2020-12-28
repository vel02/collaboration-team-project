package cs.collaboration.yescredit.ui.account.adapter.card;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.CardSelectedItemBinding;
import cs.collaboration.yescredit.ui.account.model.Card;


public class CardSelectedRecyclerAdapter extends RecyclerView.Adapter<CardSelectedBindHolder> {

    private List<Card> cards = new ArrayList<>();
    private OnCardSelectedRecyclerListener listener;

    public interface OnCardSelectedRecyclerListener {
        void onCardSelected(Card card);
    }

    public void setOnCardSelectedRecyclerListener(final OnCardSelectedRecyclerListener listener) {
        this.listener = listener;
    }

    public void refresh(List<Card> cards) {
        this.cards = cards;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CardSelectedBindHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardSelectedItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.card_selected_item, parent, false);
        return new CardSelectedBindHolder(binding.getRoot(), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CardSelectedBindHolder holder, int position) {
        holder.onBind(cards.get(position));
    }

    @Override
    public int getItemCount() {
        return ((cards != null && cards.size() > 0) ? cards.size() : 0);
    }
}
