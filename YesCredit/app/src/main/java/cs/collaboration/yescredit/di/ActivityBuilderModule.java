package cs.collaboration.yescredit.di;

import cs.collaboration.yescredit.di.ui.login.LoginModule;
import cs.collaboration.yescredit.di.ui.login.LoginViewModelModule;
import cs.collaboration.yescredit.ui.login.LoginActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(
            modules = {
                    LoginViewModelModule.class,
                    LoginModule.class
            }
    )
    abstract LoginActivity contributeLoginActivity();
}
