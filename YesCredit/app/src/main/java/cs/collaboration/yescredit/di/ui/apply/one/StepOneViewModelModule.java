package cs.collaboration.yescredit.di.ui.apply.one;

import androidx.lifecycle.ViewModel;

import cs.collaboration.yescredit.di.ViewModelKey;
import cs.collaboration.yescredit.ui.apply.fragment.one.StepOneViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class StepOneViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(StepOneViewModel.class)
    abstract ViewModel bindStepOneViewModel(StepOneViewModel viewModel);

}
