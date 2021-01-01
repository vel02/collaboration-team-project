package cs.collaboration.yescredit.di.ui.apply.eleven;

import androidx.lifecycle.ViewModel;

import cs.collaboration.yescredit.di.ViewModelKey;
import cs.collaboration.yescredit.ui.apply.fragment.eleven.ReceiptViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ReceiptViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ReceiptViewModel.class)
    abstract ViewModel bindReceiptViewModel(ReceiptViewModel viewModel);

}
