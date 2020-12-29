package cs.collaboration.yescredit.di.ui.apply.two;

import android.app.Application;

import cs.collaboration.yescredit.util.RxBackgroundImageResize;
import dagger.Module;
import dagger.Provides;

@Module
public class StepTwoModule {

    @Provides
    static RxBackgroundImageResize provideRxBackgroundImageResize(Application application) {
        return new RxBackgroundImageResize(application);
    }

}
