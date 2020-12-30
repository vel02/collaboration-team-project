package cs.collaboration.yescredit.di.ui.apply.seven;

import androidx.lifecycle.ViewModel;

import cs.collaboration.yescredit.di.ViewModelKey;
import cs.collaboration.yescredit.ui.apply.fragment.seven.SubmitViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class SubmitViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SubmitViewModel.class)
    abstract ViewModel bindSubmitViewModel(SubmitViewModel viewModel);

}
