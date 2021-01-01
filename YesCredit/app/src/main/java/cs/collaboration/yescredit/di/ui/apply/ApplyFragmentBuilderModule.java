package cs.collaboration.yescredit.di.ui.apply;

import cs.collaboration.yescredit.di.ui.apply.eight.AmountScope;
import cs.collaboration.yescredit.di.ui.apply.eight.AmountViewModelModule;
import cs.collaboration.yescredit.di.ui.apply.eleven.ReceiptScope;
import cs.collaboration.yescredit.di.ui.apply.eleven.ReceiptViewModelModule;
import cs.collaboration.yescredit.di.ui.apply.five.StepFiveScope;
import cs.collaboration.yescredit.di.ui.apply.five.StepFiveViewModelModule;
import cs.collaboration.yescredit.di.ui.apply.four.StepFourScope;
import cs.collaboration.yescredit.di.ui.apply.four.StepFourViewModelModule;
import cs.collaboration.yescredit.di.ui.apply.four.address.AddressScope;
import cs.collaboration.yescredit.di.ui.apply.four.address.AddressViewModelModule;
import cs.collaboration.yescredit.di.ui.apply.four.personal.PersonalInfoScope;
import cs.collaboration.yescredit.di.ui.apply.four.personal.PersonalInfoViewModelModule;
import cs.collaboration.yescredit.di.ui.apply.nine.ApprovedScope;
import cs.collaboration.yescredit.di.ui.apply.nine.ApprovedViewModelModule;
import cs.collaboration.yescredit.di.ui.apply.one.StepOneScope;
import cs.collaboration.yescredit.di.ui.apply.one.StepOneViewModelModule;
import cs.collaboration.yescredit.di.ui.apply.seven.SubmitScope;
import cs.collaboration.yescredit.di.ui.apply.seven.SubmitViewModelModule;
import cs.collaboration.yescredit.di.ui.apply.six.StepSixScope;
import cs.collaboration.yescredit.di.ui.apply.six.StepSixViewModelModule;
import cs.collaboration.yescredit.di.ui.apply.ten.ScheduleScope;
import cs.collaboration.yescredit.di.ui.apply.ten.ScheduleViewModelModule;
import cs.collaboration.yescredit.di.ui.apply.three.StepThreeScope;
import cs.collaboration.yescredit.di.ui.apply.three.StepThreeViewModelModule;
import cs.collaboration.yescredit.di.ui.apply.two.StepTwoDialogBuilderModule;
import cs.collaboration.yescredit.di.ui.apply.two.StepTwoModule;
import cs.collaboration.yescredit.di.ui.apply.two.StepTwoScope;
import cs.collaboration.yescredit.di.ui.apply.two.StepTwoViewModelModule;
import cs.collaboration.yescredit.ui.apply.fragment.eleven.ReceiptFragment;
import cs.collaboration.yescredit.ui.apply.fragment.eight.AmountFragment;
import cs.collaboration.yescredit.ui.apply.fragment.five.StepFiveFragment;
import cs.collaboration.yescredit.ui.apply.fragment.four.StepFourFragment;
import cs.collaboration.yescredit.ui.apply.fragment.four.address.AddressFragment;
import cs.collaboration.yescredit.ui.apply.fragment.four.personal.PersonalInfoFragment;
import cs.collaboration.yescredit.ui.apply.fragment.nine.ApprovedFragment;
import cs.collaboration.yescredit.ui.apply.fragment.one.StepOneFragment;
import cs.collaboration.yescredit.ui.apply.fragment.seven.SubmitFragment;
import cs.collaboration.yescredit.ui.apply.fragment.six.StepSixFragment;
import cs.collaboration.yescredit.ui.apply.fragment.ten.ScheduleFragment;
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

    @StepFiveScope
    @ContributesAndroidInjector(
            modules = {
                    StepFiveViewModelModule.class
            }
    )
    abstract StepFiveFragment contributeStepFiveFragment();

    @StepSixScope
    @ContributesAndroidInjector(
            modules = {
                    StepSixViewModelModule.class
            }
    )
    abstract StepSixFragment contributeStepSixFragment();

    @SubmitScope
    @ContributesAndroidInjector(
            modules = {
                    SubmitViewModelModule.class
            }
    )
    abstract SubmitFragment contributeSubmitFragment();

    @AmountScope
    @ContributesAndroidInjector(
            modules = {
                    AmountViewModelModule.class
            }
    )
    abstract AmountFragment contributeAmountFragment();

    @ApprovedScope
    @ContributesAndroidInjector(
            modules = {
                    ApprovedViewModelModule.class
            }
    )
    abstract ApprovedFragment contributeApprovedFragment();

    @ScheduleScope
    @ContributesAndroidInjector(
            modules = {
                    ScheduleViewModelModule.class
            }
    )
    abstract ScheduleFragment contributeScheduleFragment();

    @ReceiptScope
    @ContributesAndroidInjector(
            modules = {
                    ReceiptViewModelModule.class
            }
    )
    abstract ReceiptFragment contributeReceiptFragment();

}
