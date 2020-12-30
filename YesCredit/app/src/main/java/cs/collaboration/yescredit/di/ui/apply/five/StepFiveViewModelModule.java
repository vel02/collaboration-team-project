package cs.collaboration.yescredit.di.ui.apply.five;

import androidx.lifecycle.ViewModel;

import cs.collaboration.yescredit.di.ViewModelKey;
import cs.collaboration.yescredit.ui.apply.fragment.five.StepFiveViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class StepFiveViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(StepFiveViewModel.class)
    abstract ViewModel bindStepFiveViewModel(StepFiveViewModel viewModel);

}
