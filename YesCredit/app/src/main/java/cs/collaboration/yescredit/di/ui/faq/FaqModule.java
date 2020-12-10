package cs.collaboration.yescredit.di.ui.faq;

import androidx.databinding.DataBindingUtil;

import javax.inject.Named;

import cs.collaboration.yescredit.databinding.ActivityFaqBinding;
import cs.collaboration.yescredit.ui.faq.FaqActivity;
import dagger.Module;
import dagger.Provides;

@Module
public class FaqModule {

    @Provides
    static ActivityFaqBinding provideActivityFaqBinding(FaqActivity activity, @Named("Faq Activity") int layout) {
        return DataBindingUtil.setContentView(activity, layout);
    }

}
