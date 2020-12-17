package cs.collaboration.yescredit.di.ui.allowable;

import androidx.lifecycle.ViewModel;

import cs.collaboration.yescredit.di.ViewModelKey;
import cs.collaboration.yescredit.ui.allowable.AllowableViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class AllowableViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AllowableViewModel.class)
    abstract ViewModel bindAllowableViewModel(AllowableViewModel viewModel);

}
