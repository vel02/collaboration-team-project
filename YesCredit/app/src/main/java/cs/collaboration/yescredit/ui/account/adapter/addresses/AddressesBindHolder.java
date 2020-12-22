package cs.collaboration.yescredit.ui.account.adapter.addresses;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import cs.collaboration.yescredit.databinding.AddressesItemBinding;
import cs.collaboration.yescredit.model.Address;
import cs.collaboration.yescredit.ui.account.adapter.BaseBindHolder;

public class AddressesBindHolder extends BaseBindHolder {

    private AddressesItemBinding binding;

    public AddressesBindHolder(@NonNull View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
    }

    @Override
    protected void clear() {
        binding.addressStreet.setText("");
        binding.addressBarangay.setText("");
        binding.addressCity.setText("");
        binding.addressZipcode.setText("");
        binding.addressProvince.setText("");
    }

    @Override
    protected void initialization() {

    }

    @Override
    public void onBind(Address address) {
        super.onBind(address);
        binding.setAddresses(address);
    }
}
