package cs.collaboration.yescredit.di.ui.account.information.card;

import androidx.lifecycle.ViewModel;

import cs.collaboration.yescredit.di.ViewModelKey;
import cs.collaboration.yescredit.ui.account.fragment.information.CardAccountViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class CardAccountViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(CardAccountViewModel.class)
    abstract ViewModel bindCardAccountViewModel(CardAccountViewModel viewModel);

}
