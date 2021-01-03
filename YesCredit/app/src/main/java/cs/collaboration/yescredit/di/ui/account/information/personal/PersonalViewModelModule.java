package cs.collaboration.yescredit.di.ui.account.information.personal;

import androidx.lifecycle.ViewModel;

import cs.collaboration.yescredit.di.ViewModelKey;
import cs.collaboration.yescredit.ui.account.fragment.information.PersonalViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class PersonalViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(PersonalViewModel.class)
    abstract ViewModel bindPersonalViewModel(PersonalViewModel viewModel);

}
