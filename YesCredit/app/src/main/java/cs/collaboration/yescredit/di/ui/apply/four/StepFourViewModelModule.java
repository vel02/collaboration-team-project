package cs.collaboration.yescredit.di.ui.apply.four;

import androidx.lifecycle.ViewModel;

import cs.collaboration.yescredit.di.ViewModelKey;
import cs.collaboration.yescredit.ui.apply.fragment.four.StepFourViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class StepFourViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(StepFourViewModel.class)
    abstract ViewModel bindStepFourViewModel(StepFourViewModel viewModel);

}
