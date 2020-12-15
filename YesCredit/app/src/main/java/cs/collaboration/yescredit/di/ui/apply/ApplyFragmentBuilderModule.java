package cs.collaboration.yescredit.di.ui.apply;

import cs.collaboration.yescredit.di.ui.apply.two.StepTwoDialogBuilderModule;
import cs.collaboration.yescredit.ui.apply.fragment.StepFourFragment;
import cs.collaboration.yescredit.ui.apply.fragment.StepOneFragment;
import cs.collaboration.yescredit.ui.apply.fragment.StepThreeFragment;
import cs.collaboration.yescredit.ui.apply.fragment.StepTwoFragment;
import cs.collaboration.yescredit.ui.apply.fragment.four.PersonalInfoFragment;
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

    @ContributesAndroidInjector
    abstract StepFourFragment contributeStepFourFragment();

    @ContributesAndroidInjector
    abstract PersonalInfoFragment contributePersonalInfoFragment();

}
