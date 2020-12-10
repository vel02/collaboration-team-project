package cs.collaboration.yescredit.di.ui.home;

import androidx.databinding.DataBindingUtil;

import javax.inject.Named;

import cs.collaboration.yescredit.databinding.ActivityHomeBinding;
import cs.collaboration.yescredit.ui.home.HomeActivity;
import dagger.Module;
import dagger.Provides;

@Module
public class HomeModule {

    @Provides
    static ActivityHomeBinding provideActivityHomeBinding(HomeActivity activity, @Named("Home Activity") int layout) {
        return DataBindingUtil.setContentView(activity, layout);
    }

}
