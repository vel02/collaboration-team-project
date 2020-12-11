package cs.collaboration.yescredit.di.ui.referral;

import androidx.databinding.DataBindingUtil;

import javax.inject.Named;

import cs.collaboration.yescredit.databinding.ActivityReferralBinding;
import cs.collaboration.yescredit.ui.referral.ReferralActivity;
import dagger.Module;
import dagger.Provides;

@Module
public class ReferralModule {

    @Provides
    static ActivityReferralBinding provideActivityReferralBinding(ReferralActivity activity, @Named("Referral Activity") int layout) {
        return DataBindingUtil.setContentView(activity, layout);
    }

}
