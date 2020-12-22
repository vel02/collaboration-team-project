package cs.collaboration.yescredit.ui.account.adapter.address;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.AddressItemBinding;
import cs.collaboration.yescredit.model.Address;

public class AddressRecyclerAdapter extends RecyclerView.Adapter<AddressBindHolder> {

    private List<Address> addresses = new ArrayList<>();
    private OnAddressRecyclerListener listener;

    public void refresh(List<Address> addresses) {
        this.addresses = addresses;
        notifyDataSetChanged();
    }

    public void setOnAddressRecyclerListener(final OnAddressRecyclerListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public AddressBindHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AddressItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.address_item, parent, false);
        return new AddressBindHolder(binding.getRoot(), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressBindHolder holder, int position) {
        holder.onBind(addresses.get(position));
    }

    @Override
    public int getItemCount() {
        return ((addresses != null && addresses.size() > 0) ? addresses.size() : 0);
    }

    public interface OnAddressRecyclerListener {
        void onClickAddress(Address address);
    }
}
