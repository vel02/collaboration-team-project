package cs.collaboration.yescredit.ui.account.adapter.card;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import cs.collaboration.yescredit.databinding.CardItemBinding;
import cs.collaboration.yescredit.ui.account.adapter.BaseBindHolder;
import cs.collaboration.yescredit.ui.account.model.Card;

import static cs.collaboration.yescredit.ui.account.adapter.card.CardRecyclerAdapter.*;

public class CardBindHolder extends BaseBindHolder {

    private final CardItemBinding binding;
    private final OnCardRecyclerListener listener;

    public CardBindHolder(@NonNull View itemView, final OnCardRecyclerListener listener) {
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
        binding.setListener((OnCardRecyclerListener) listener);

        //retrieved card in database here...

    }

    @Override
    public void onBind(Card card) {
        super.onBind(card);
        binding.setCard(card);
    }
}
