package cs.collaboration.yescredit.di.ui.account.information.personal.address;

import androidx.lifecycle.ViewModel;

import cs.collaboration.yescredit.di.ViewModelKey;
import cs.collaboration.yescredit.ui.account.fragment.information.personal.addresses.AddAddressViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class AddAddressViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AddAddressViewModel.class)
    abstract ViewModel bindAddAddressViewModel(AddAddressViewModel viewModel);

}
