package cs.collaboration.yescredit.ui.account.adapter.addresses;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.AddressesItemBinding;
import cs.collaboration.yescredit.model.Address;

public class AddressesRecyclerAdapter extends RecyclerView.Adapter<AddressesBindHolder> {

    private List<Address> addresses = new ArrayList<>();

    public void refresh(List<Address> addresses) {
        this.addresses = addresses;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AddressesBindHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AddressesItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.addresses_item, parent, false);
        return new AddressesBindHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull AddressesBindHolder holder, int position) {
        holder.onBind(addresses.get(position));
    }

    @Override
    public int getItemCount() {
        return ((addresses != null && addresses.size() > 0) ? addresses.size() : 0);
    }

}
