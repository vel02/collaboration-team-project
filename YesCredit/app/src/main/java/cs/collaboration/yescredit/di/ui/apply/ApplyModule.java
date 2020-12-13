package cs.collaboration.yescredit.di.ui.apply;

import androidx.databinding.DataBindingUtil;

import javax.inject.Named;

import cs.collaboration.yescredit.databinding.ActivityApplyBinding;
import cs.collaboration.yescredit.ui.apply.ApplyActivity;
import cs.collaboration.yescredit.ui.apply.SessionManager;
import dagger.Module;
import dagger.Provides;

@Module
public class ApplyModule {


    @Provides
    static ActivityApplyBinding provideActivityApplyBinding(ApplyActivity activity, @Named("Apply Activity") int layout) {
        return DataBindingUtil.setContentView(activity, layout);
    }

    @Provides
    static SessionManager sessionManager() {
        return new SessionManager();
    }

}
