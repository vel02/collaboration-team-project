package cs.collaboration.yescredit.di.ui.apply;

import cs.collaboration.yescredit.di.ui.apply.four.StepFourScope;
import cs.collaboration.yescredit.di.ui.apply.four.StepFourViewModelModule;
import cs.collaboration.yescredit.di.ui.apply.four.address.AddressScope;
import cs.collaboration.yescredit.di.ui.apply.four.address.AddressViewModelModule;
import cs.collaboration.yescredit.di.ui.apply.four.personal.PersonalInfoScope;
import cs.collaboration.yescredit.di.ui.apply.four.personal.PersonalInfoViewModelModule;
import cs.collaboration.yescredit.di.ui.apply.one.StepOneScope;
import cs.collaboration.yescredit.di.ui.apply.one.StepOneViewModelModule;
import cs.collaboration.yescredit.di.ui.apply.three.StepThreeScope;
import cs.collaboration.yescredit.di.ui.apply.three.StepThreeViewModelModule;
import cs.collaboration.yescredit.di.ui.apply.two.StepTwoDialogBuilderModule;
import cs.collaboration.yescredit.di.ui.apply.two.StepTwoModule;
import cs.collaboration.yescredit.di.ui.apply.two.StepTwoScope;
import cs.collaboration.yescredit.di.ui.apply.two.StepTwoViewModelModule;
import cs.collaboration.yescredit.ui.apply.fragment.AmountFragment;
import cs.collaboration.yescredit.ui.apply.fragment.ApprovedFragment;
import cs.collaboration.yescredit.ui.apply.fragment.ReceiptFragment;
import cs.collaboration.yescredit.ui.apply.fragment.ScheduleFragment;
import cs.collaboration.yescredit.ui.apply.fragment.StepFiveFragment;
import cs.collaboration.yescredit.ui.apply.fragment.StepSixFragment;
import cs.collaboration.yescredit.ui.apply.fragment.SubmitFragment;
import cs.collaboration.yescredit.ui.apply.fragment.four.address.AddressFragment;
import cs.collaboration.yescredit.ui.apply.fragment.four.StepFourFragment;
import cs.collaboration.yescredit.ui.apply.fragment.four.personal.PersonalInfoFragment;
import cs.collaboration.yescredit.ui.apply.fragment.one.StepOneFragment;
import cs.collaboration.yescredit.ui.apply.fragment.three.StepThreeFragment;
import cs.collaboration.yescredit.ui.apply.fragment.two.StepTwoFragment;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;


@Module
public abstract class ApplyFragmentBuilderModule {

    @StepOneScope
    @ContributesAndroidInjector(
            modules = {
                    StepOneViewModelModule.class
            }
    )
    abstract StepOneFragment contributeStepOneFragment();

    @StepTwoScope
    @ContributesAndroidInjector(
            modules = {
                    StepTwoDialogBuilderModule.class,
                    StepTwoViewModelModule.class,
                    StepTwoModule.class
            }
    )
    abstract StepTwoFragment contributeStepTwoFragment();

    @StepThreeScope
    @ContributesAndroidInjector(
            modules = {
                    StepThreeViewModelModule.class
            }
    )
    abstract StepThreeFragment contributeStepThreeFragment();

    @StepFourScope
    @ContributesAndroidInjector(
            modules = {
                    StepFourViewModelModule.class
            }
    )
    abstract StepFourFragment contributeStepFourFragment();

    @PersonalInfoScope
    @ContributesAndroidInjector(
            modules = {
                    PersonalInfoViewModelModule.class
            }
    )
    abstract PersonalInfoFragment contributePersonalInfoFragment();

    @AddressScope
    @ContributesAndroidInjector(
            modules = {
                    AddressViewModelModule.class
            }
    )
    abstract AddressFragment contributeAddressFragment();

    @ContributesAndroidInjector
    abstract StepFiveFragment contributeStepFiveFragment();

    @ContributesAndroidInjector
    abstract StepSixFragment contributeStepSixFragment();

    @ContributesAndroidInjector
    abstract SubmitFragment contributeSubmitFragment();

    @ContributesAndroidInjector
    abstract AmountFragment contributeAmountFragment();

    @ContributesAndroidInjector
    abstract ApprovedFragment contributeApprovedFragment();

    @ContributesAndroidInjector
    abstract ScheduleFragment contributeScheduleFragment();

    @ContributesAndroidInjector
    abstract ReceiptFragment contributeReceiptFragment();

}
