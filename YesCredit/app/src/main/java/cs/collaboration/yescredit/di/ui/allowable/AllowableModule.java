package cs.collaboration.yescredit.di.ui.allowable;

import androidx.databinding.DataBindingUtil;

import javax.inject.Named;

import cs.collaboration.yescredit.databinding.ActivityAllowableBinding;
import cs.collaboration.yescredit.ui.allowable.AllowableActivity;
import dagger.Module;
import dagger.Provides;

@Module
public class AllowableModule {

    @Provides
    static ActivityAllowableBinding provideActivityAllowableBinding(AllowableActivity activity, @Named("Allowable Activity") int layout) {
        return DataBindingUtil.setContentView(activity, layout);
    }

}
