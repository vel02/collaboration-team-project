package cs.collaboration.yescredit.di.ui.faq;

import androidx.lifecycle.ViewModel;

import cs.collaboration.yescredit.di.ViewModelKey;
import cs.collaboration.yescredit.ui.faq.FaqViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class FaqViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(FaqViewModel.class)
    abstract ViewModel bindFaqViewModel(FaqViewModel viewModel);
}
