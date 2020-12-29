package cs.collaboration.yescredit.di.ui.apply.three;

import androidx.lifecycle.ViewModel;

import cs.collaboration.yescredit.di.ViewModelKey;
import cs.collaboration.yescredit.ui.apply.fragment.three.StepThreeViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class StepThreeViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(StepThreeViewModel.class)
    abstract ViewModel bindStepThreeViewModel(StepThreeViewModel viewModel);

}
