package cs.collaboration.yescredit.di.ui.account.information.preference;

import androidx.lifecycle.ViewModel;

import cs.collaboration.yescredit.di.ViewModelKey;
import cs.collaboration.yescredit.ui.account.fragment.information.PaymentPreferenceViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class PaymentPreferenceViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(PaymentPreferenceViewModel.class)
    abstract ViewModel bindPaymentPreferenceViewModel(PaymentPreferenceViewModel viewModel);

}
