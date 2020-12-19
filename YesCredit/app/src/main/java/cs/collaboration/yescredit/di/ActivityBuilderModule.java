package cs.collaboration.yescredit.di;

import cs.collaboration.yescredit.di.ui.account.AccountFragmentBuilderModule;
import cs.collaboration.yescredit.di.ui.account.AccountModule;
import cs.collaboration.yescredit.di.ui.account.AccountScope;
import cs.collaboration.yescredit.di.ui.allowable.AllowableModule;
import cs.collaboration.yescredit.di.ui.allowable.AllowableScope;
import cs.collaboration.yescredit.di.ui.allowable.AllowableViewModelModule;
import cs.collaboration.yescredit.di.ui.apply.ApplyFragmentBuilderModule;
import cs.collaboration.yescredit.di.ui.apply.ApplyModule;
import cs.collaboration.yescredit.di.ui.apply.ApplyScope;
import cs.collaboration.yescredit.di.ui.apply.ApplyViewModelModule;
import cs.collaboration.yescredit.di.ui.existing.ExistingLoanModule;
import cs.collaboration.yescredit.di.ui.existing.ExistingLoanScope;
import cs.collaboration.yescredit.di.ui.existing.ExistingLoanViewModelModule;
import cs.collaboration.yescredit.di.ui.faq.FaqModule;
import cs.collaboration.yescredit.di.ui.faq.FaqScope;
import cs.collaboration.yescredit.di.ui.faq.FaqViewModelModule;
import cs.collaboration.yescredit.di.ui.home.HomeModule;
import cs.collaboration.yescredit.di.ui.home.HomeScope;
import cs.collaboration.yescredit.di.ui.home.HomeViewModelModule;
import cs.collaboration.yescredit.di.ui.login.LoginDialogBuilderModule;
import cs.collaboration.yescredit.di.ui.login.LoginModule;
import cs.collaboration.yescredit.di.ui.login.LoginScope;
import cs.collaboration.yescredit.di.ui.login.LoginViewModelModule;
import cs.collaboration.yescredit.di.ui.referral.ReferralModule;
import cs.collaboration.yescredit.di.ui.referral.ReferralViewModelModule;
import cs.collaboration.yescredit.di.ui.signup.SignUpModule;
import cs.collaboration.yescredit.di.ui.signup.SignUpScope;
import cs.collaboration.yescredit.di.ui.signup.SignUpViewModelModule;
import cs.collaboration.yescredit.ui.account.AccountSettingsActivity;
import cs.collaboration.yescredit.ui.allowable.AllowableActivity;
import cs.collaboration.yescredit.ui.apply.ApplyActivity;
import cs.collaboration.yescredit.ui.existing.ExistingLoanActivity;
import cs.collaboration.yescredit.ui.faq.FaqActivity;
import cs.collaboration.yescredit.ui.home.HomeActivity;
import cs.collaboration.yescredit.ui.login.LoginActivity;
import cs.collaboration.yescredit.ui.referral.ReferralActivity;
import cs.collaboration.yescredit.ui.signup.SignUpActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilderModule {

    @LoginScope
    @ContributesAndroidInjector(
            modules = {
                    LoginDialogBuilderModule.class,
                    LoginViewModelModule.class,
                    LoginModule.class
            }
    )
    abstract LoginActivity contributeLoginActivity();

    @SignUpScope
    @ContributesAndroidInjector(
            modules = {
                    SignUpViewModelModule.class,
                    SignUpModule.class
            }
    )
    abstract SignUpActivity contributeSignUpActivity();

    @HomeScope
    @ContributesAndroidInjector(
            modules = {
                    HomeViewModelModule.class,
                    HomeModule.class
            }
    )
    abstract HomeActivity contributeHomeActivity();

    @FaqScope
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

    @ApplyScope
    @ContributesAndroidInjector(
            modules = {
                    ApplyFragmentBuilderModule.class,
                    ApplyViewModelModule.class,
                    ApplyModule.class
            }
    )
    abstract ApplyActivity contributeApplyActivity();


    @AllowableScope
    @ContributesAndroidInjector(
            modules = {
                    AllowableViewModelModule.class,
                    AllowableModule.class
            }
    )
    abstract AllowableActivity contributeAllowableActivity();

    @ExistingLoanScope
    @ContributesAndroidInjector(
            modules = {
                    ExistingLoanViewModelModule.class,
                    ExistingLoanModule.class
            }
    )
    abstract ExistingLoanActivity contributeExistingLoneActivity();

    @AccountScope
    @ContributesAndroidInjector(
            modules = {
                    AccountFragmentBuilderModule.class,
                    AccountModule.class
            }
    )
    abstract AccountSettingsActivity contributeAccountSettingsActivity();
}
