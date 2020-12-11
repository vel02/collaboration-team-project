package cs.collaboration.yescredit.di;

import cs.collaboration.yescredit.di.ui.faq.FaqModule;
import cs.collaboration.yescredit.di.ui.faq.FaqViewModelModule;
import cs.collaboration.yescredit.di.ui.home.HomeModule;
import cs.collaboration.yescredit.di.ui.home.HomeViewModelModule;
import cs.collaboration.yescredit.di.ui.login.LoginDialogBuilderModule;
import cs.collaboration.yescredit.di.ui.login.LoginModule;
import cs.collaboration.yescredit.di.ui.login.LoginViewModelModule;
import cs.collaboration.yescredit.di.ui.referral.ReferralModule;
import cs.collaboration.yescredit.di.ui.referral.ReferralViewModelModule;
import cs.collaboration.yescredit.di.ui.signup.SignUpModule;
import cs.collaboration.yescredit.di.ui.signup.SignUpViewModelModule;
import cs.collaboration.yescredit.ui.faq.FaqActivity;
import cs.collaboration.yescredit.ui.home.HomeActivity;
import cs.collaboration.yescredit.ui.login.LoginActivity;
import cs.collaboration.yescredit.ui.referral.ReferralActivity;
import cs.collaboration.yescredit.ui.signup.SignUpActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(
            modules = {
                    LoginDialogBuilderModule.class,
                    LoginViewModelModule.class,
                    LoginModule.class
            }
    )
    abstract LoginActivity contributeLoginActivity();

    @ContributesAndroidInjector(
            modules = {
                    SignUpViewModelModule.class,
                    SignUpModule.class
            }
    )
    abstract SignUpActivity contributeSignUpActivity();

    @ContributesAndroidInjector(
            modules = {
                    HomeViewModelModule.class,
                    HomeModule.class
            }
    )
    abstract HomeActivity contributeHomeActivity();

    @ContributesAndroidInjector(
            modules = {
                    FaqViewModelModule.class,
                    FaqModule.class
            }
    )
    abstract FaqActivity contributeFaqActivity();

    @ContributesAndroidInjector(
            modules = {
                    ReferralViewModelModule.class,
                    ReferralModule.class
            }
    )
    abstract ReferralActivity contributeReferralActivity();
}
