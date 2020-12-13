package cs.collaboration.yescredit.di.ui.apply;

import androidx.lifecycle.ViewModel;

import cs.collaboration.yescredit.di.ViewModelKey;
import cs.collaboration.yescredit.ui.apply.ApplyViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ApplyViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ApplyViewModel.class)
    abstract ViewModel bindApplyViewModel(ApplyViewModel viewModel);

}
