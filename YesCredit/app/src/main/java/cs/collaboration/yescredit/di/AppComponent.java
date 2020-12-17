package cs.collaboration.yescredit.di;

import android.app.Application;

import javax.inject.Named;
import javax.inject.Singleton;

import cs.collaboration.yescredit.BaseApplication;
import cs.collaboration.yescredit.ui.apply.SessionManager;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(
        modules = {
                AndroidSupportInjectionModule.class,
                ViewModelFactoryModule.class,
                ActivityBuilderModule.class,
                AppModule.class
        }
)
public interface AppComponent extends AndroidInjector<BaseApplication> {

    SessionManager sessionManager();

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        @BindsInstance
        Builder loginActivity(@Named("Login Activity") int layout);

        @BindsInstance
        Builder signUpActivity(@Named("Sign Up Activity") int layout);

        @BindsInstance
        Builder homeActivity(@Named("Home Activity") int layout);

        @BindsInstance
        Builder faqActivity(@Named("Faq Activity") int layout);

        @BindsInstance
        Builder referralActivity(@Named("Referral Activity") int layout);

        @BindsInstance
        Builder allowableActivity(@Named("Allowable Activity") int layout);

        @BindsInstance
        Builder existingLoanActivity(@Named("Existing Loan Activity") int layout);

        AppComponent build();
    }

}
