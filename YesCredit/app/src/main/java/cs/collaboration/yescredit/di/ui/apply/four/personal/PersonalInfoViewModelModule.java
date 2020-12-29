package cs.collaboration.yescredit.di.ui.apply.four.personal;

import androidx.lifecycle.ViewModel;

import cs.collaboration.yescredit.di.ViewModelKey;
import cs.collaboration.yescredit.ui.apply.fragment.four.personal.PersonalInfoViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class PersonalInfoViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(PersonalInfoViewModel.class)
    abstract ViewModel bindPersonalInfoViewModel(PersonalInfoViewModel viewModel);

}
