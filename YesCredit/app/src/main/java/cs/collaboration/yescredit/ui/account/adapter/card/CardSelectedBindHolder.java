package cs.collaboration.yescredit.ui.account.adapter.card;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import cs.collaboration.yescredit.databinding.CardSelectedItemBinding;
import cs.collaboration.yescredit.ui.account.adapter.BaseBindHolder;
import cs.collaboration.yescredit.ui.account.model.Card;

import static cs.collaboration.yescredit.ui.account.adapter.card.CardSelectedRecyclerAdapter.*;

public class CardSelectedBindHolder extends BaseBindHolder {

    private final CardSelectedItemBinding binding;
    private final OnCardSelectedRecyclerListener listener;

    public CardSelectedBindHolder(@NonNull View itemView, OnCardSelectedRecyclerListener listener) {
        super(itemView);
        this.listener = listener;
        binding = DataBindingUtil.bind(itemView);
    }

    @Override
    protected void clear() {
        binding.cardName.setText("");
        binding.cardNumber.setText("");
    }

    @Override
    protected void initialization() {
        binding.setListener(listener);
    }

    @Override
    public void onBind(Card card) {
        super.onBind(card);
        binding.setCard(card);
    }
}
