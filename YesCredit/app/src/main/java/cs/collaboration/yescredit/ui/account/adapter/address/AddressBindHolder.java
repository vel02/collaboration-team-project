package cs.collaboration.yescredit.ui.account.adapter.address;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import cs.collaboration.yescredit.databinding.AddressItemBinding;
import cs.collaboration.yescredit.model.Address;
import cs.collaboration.yescredit.ui.account.adapter.BaseBindHolder;

import static cs.collaboration.yescredit.ui.account.adapter.address.AddressRecyclerAdapter.*;

public class AddressBindHolder extends BaseBindHolder {

    private final AddressItemBinding binding;
    private final OnAddressRecyclerListener listener;

    public AddressBindHolder(@NonNull View itemView, final OnAddressRecyclerListener listener) {
        super(itemView);
        this.listener = listener;
        binding = DataBindingUtil.bind(itemView);
    }

    @Override
    protected void clear() {
        binding.addressStreet.setText("");
        binding.addressBarangay.setText("");
        binding.addressCity.setText("");
        binding.addressZipcode.setText("");
        binding.addressProvince.setText("");
        binding.addressStatus.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void initialization() {
        binding.setListener((OnAddressRecyclerListener) listener);
    }

    @Override
    public void onBind(Address address) {
        super.onBind(address);
        binding.setAddress(address);
    }
}
