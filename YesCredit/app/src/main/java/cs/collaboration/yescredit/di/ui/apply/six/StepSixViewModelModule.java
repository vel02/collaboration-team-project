package cs.collaboration.yescredit.di.ui.apply.six;

import androidx.lifecycle.ViewModel;

import cs.collaboration.yescredit.di.ViewModelKey;
import cs.collaboration.yescredit.ui.apply.fragment.six.StepSixViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class StepSixViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(StepSixViewModel.class)
    abstract ViewModel bindStepSixViewModel(StepSixViewModel viewModel);

}
