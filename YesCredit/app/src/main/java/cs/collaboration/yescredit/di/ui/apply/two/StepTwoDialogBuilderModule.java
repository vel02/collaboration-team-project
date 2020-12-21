package cs.collaboration.yescredit.di.ui.apply.two;

import cs.collaboration.yescredit.ui.apply.dialog.GeneratePhotoFragment;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class StepTwoDialogBuilderModule {

    @ContributesAndroidInjector
    abstract GeneratePhotoFragment contributeGovernmentPhotoFragment();

}
