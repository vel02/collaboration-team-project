package cs.collaboration.yescredit.di.ui.apply.eight;

import androidx.lifecycle.ViewModel;

import cs.collaboration.yescredit.di.ViewModelKey;
import cs.collaboration.yescredit.ui.apply.fragment.eight.AmountViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class AmountViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AmountViewModel.class)
    abstract ViewModel bindAmountViewModel(AmountViewModel viewModel);

}
