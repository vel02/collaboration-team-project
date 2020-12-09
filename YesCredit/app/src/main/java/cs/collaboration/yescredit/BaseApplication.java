package cs.collaboration.yescredit;

import cs.collaboration.yescredit.di.DaggerAppComponent;
import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

public class BaseApplication extends DaggerApplication {
    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this)
                .loginActivity(R.layout.activity_login)
                .build();
    }
}
