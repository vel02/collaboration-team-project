package cs.collaboration.yescredit;

import cs.collaboration.yescredit.di.DaggerAppComponent;
import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

public class BaseApplication extends DaggerApplication {
    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this)
                .loginActivity(R.layout.activity_login)
                .signUpActivity(R.layout.activity_sign_up)
                .homeActivity(R.layout.activity_home)
                .faqActivity(R.layout.activity_faq)
                .referralActivity(R.layout.activity_referral)
                .allowableActivity(R.layout.activity_allowable)
                .existingLoanActivity(R.layout.activity_existing_loan)
                .paymentActivity(R.layout.activity_payment)
                .build();
    }
}
