package cs.collaboration.yescredit.di.ui.signup;

import androidx.databinding.DataBindingUtil;

import javax.inject.Named;

import cs.collaboration.yescredit.databinding.ActivitySignUpBinding;
import cs.collaboration.yescredit.ui.signup.SignUpActivity;
import dagger.Module;
import dagger.Provides;

@Module
public class SignUpModule {

    @Provides
    static ActivitySignUpBinding provideActivitySignUpBinding(SignUpActivity activity, @Named("Sign Up Activity") int layout) {
        return DataBindingUtil.setContentView(activity, layout);
    }
}
