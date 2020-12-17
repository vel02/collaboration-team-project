package cs.collaboration.yescredit.di.ui.existing;

import androidx.lifecycle.ViewModel;

import cs.collaboration.yescredit.di.ViewModelKey;
import cs.collaboration.yescredit.ui.existing.ExistingLoanViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ExistingLoanViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ExistingLoanViewModel.class)
    abstract ViewModel bindExistingLoanViewModel(ExistingLoanViewModel viewModel);

}
