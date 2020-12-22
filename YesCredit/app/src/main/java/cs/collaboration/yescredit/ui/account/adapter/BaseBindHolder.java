package cs.collaboration.yescredit.ui.account.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cs.collaboration.yescredit.model.Address;
import cs.collaboration.yescredit.ui.account.model.Card;

public abstract class BaseBindHolder extends RecyclerView.ViewHolder {

    private Card card;
    private Address address;

    public BaseBindHolder(@NonNull View itemView) {
        super(itemView);
    }

    protected abstract void clear();

    protected abstract void initialization();

    public void onBind(Card card) {
        this.card = card;
        this.initialization();
        this.clear();
    }

    public void onBind(Address address) {
        this.address = address;
        this.initialization();
        this.clear();
    }

    public Card getCard() {
        return card;
    }

    public Address getAddress() {
        return address;
    }
}
