package cs.collaboration.yescredit.di.ui.apply;

import cs.collaboration.yescredit.di.ui.apply.two.StepTwoDialogBuilderModule;
import cs.collaboration.yescredit.ui.apply.fragment.StepOneFragment;
import cs.collaboration.yescredit.ui.apply.fragment.StepThreeFragment;
import cs.collaboration.yescredit.ui.apply.fragment.StepTwoFragment;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ApplyFragmentBuilderModule {

    @ContributesAndroidInjector
    abstract StepOneFragment contributeStepOneFragment();

    @ContributesAndroidInjector(
            modules = {
                    StepTwoDialogBuilderModule.class
            }
    )
    abstract StepTwoFragment contributeStepTwoFragment();

    @ContributesAndroidInjector
    abstract StepThreeFragment contributeStepThreeFragment();

}
