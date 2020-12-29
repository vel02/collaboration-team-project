package cs.collaboration.yescredit.di.ui.apply.two;

import androidx.lifecycle.ViewModel;

import cs.collaboration.yescredit.di.ViewModelKey;
import cs.collaboration.yescredit.ui.apply.fragment.two.StepTwoViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class StepTwoViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(StepTwoViewModel.class)
    abstract ViewModel bindStepTwoViewModel(StepTwoViewModel viewModel);

}
