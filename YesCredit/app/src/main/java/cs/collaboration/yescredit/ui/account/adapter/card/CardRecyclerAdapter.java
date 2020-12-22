package cs.collaboration.yescredit.ui.account.adapter.card;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.CardItemBinding;
import cs.collaboration.yescredit.ui.account.model.Card;

public class CardRecyclerAdapter extends RecyclerView.Adapter<CardBindHolder> {

    private List<Card> cards = new ArrayList<>();
    private OnCardRecyclerListener listener;

    public void refresh(List<Card> cards) {
        this.cards = cards;
        notifyDataSetChanged();
    }

    public void setOnCardRecyclerListener(final OnCardRecyclerListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CardBindHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.card_item,
                        parent, false);
        return new CardBindHolder(binding.getRoot(), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CardBindHolder holder, int position) {
        holder.onBind(cards.get(position));
    }

    @Override
    public int getItemCount() {
        return ((cards != null && cards.size() > 0) ? cards.size() : 0);
    }

    public interface OnCardRecyclerListener {
        void onCardClick(Card card);
    }
}
