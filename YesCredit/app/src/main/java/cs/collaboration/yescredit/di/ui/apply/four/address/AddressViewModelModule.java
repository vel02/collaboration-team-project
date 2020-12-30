package cs.collaboration.yescredit.di.ui.apply.four.address;

import androidx.lifecycle.ViewModel;

import cs.collaboration.yescredit.di.ViewModelKey;
import cs.collaboration.yescredit.ui.apply.fragment.four.address.AddressViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class AddressViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AddressViewModel.class)
    abstract ViewModel bindAddressViewModel(AddressViewModel viewModel);

}
