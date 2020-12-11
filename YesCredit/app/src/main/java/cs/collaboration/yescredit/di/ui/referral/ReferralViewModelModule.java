package cs.collaboration.yescredit.di.ui.referral;

import androidx.lifecycle.ViewModel;

import cs.collaboration.yescredit.di.ViewModelKey;
import cs.collaboration.yescredit.ui.referral.ReferralViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ReferralViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ReferralViewModel.class)
    abstract ViewModel bindReferralViewModel(ReferralViewModel viewModel);
}
