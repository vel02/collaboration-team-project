package cs.collaboration.yescredit.di.ui.apply.nine;

import androidx.lifecycle.ViewModel;

import cs.collaboration.yescredit.di.ViewModelKey;
import cs.collaboration.yescredit.ui.apply.fragment.nine.ApprovedViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ApprovedViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ApprovedViewModel.class)
    abstract ViewModel bindApprovedViewModel(ApprovedViewModel viewModel);

}
