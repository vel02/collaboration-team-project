package cs.collaboration.yescredit.di.ui.account.information.personal.addresses;

import androidx.lifecycle.ViewModel;

import cs.collaboration.yescredit.di.ViewModelKey;
import cs.collaboration.yescredit.ui.account.fragment.information.personal.AddressesViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class AddressesViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AddressesViewModel.class)
    abstract ViewModel bindAddressesViewModel(AddressesViewModel viewModel);

}
