package cs.collaboration.yescredit.di.ui.login;

import androidx.databinding.DataBindingUtil;

import javax.inject.Named;

import cs.collaboration.yescredit.databinding.ActivityLoginBinding;
import cs.collaboration.yescredit.ui.login.LoginActivity;
import dagger.Module;
import dagger.Provides;

@Module
public class LoginModule {

    @Provides
    static ActivityLoginBinding provideActivityLoginBinding(LoginActivity activity, @Named("Login Activity") int layout) {
        return DataBindingUtil.setContentView(activity, layout);
    }


}
