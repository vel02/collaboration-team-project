package cs.collaboration.yescredit.di.ui.account.information;

import androidx.lifecycle.ViewModel;

import cs.collaboration.yescredit.di.ViewModelKey;
import cs.collaboration.yescredit.ui.account.fragment.InformationViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class InformationViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(InformationViewModel.class)
    abstract ViewModel bindInformationViewModel(InformationViewModel viewModel);

}
