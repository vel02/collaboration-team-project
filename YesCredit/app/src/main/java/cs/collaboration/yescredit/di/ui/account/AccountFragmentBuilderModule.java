package cs.collaboration.yescredit.di.ui.account;

import cs.collaboration.yescredit.ui.account.fragment.information.CardAccountFragment;
import cs.collaboration.yescredit.ui.account.fragment.InformationFragment;
import cs.collaboration.yescredit.ui.account.fragment.information.PersonalFragment;
import cs.collaboration.yescredit.ui.account.fragment.information.card.AddBillingFragment;
import cs.collaboration.yescredit.ui.account.fragment.information.card.AddCardFragment;
import cs.collaboration.yescredit.ui.account.fragment.information.card.BillingAddressFragment;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class AccountFragmentBuilderModule {

    @ContributesAndroidInjector
    abstract InformationFragment contributeInformationFragment();

    @ContributesAndroidInjector
    abstract PersonalFragment contributePersonalFragment();

    @ContributesAndroidInjector
    abstract CardAccountFragment contributeCardAccountFragment();

    @ContributesAndroidInjector
    abstract AddCardFragment contributeAddCardFragment();

    @ContributesAndroidInjector
    abstract BillingAddressFragment contributeBillingAddressFragment();

    @ContributesAndroidInjector
    abstract AddBillingFragment contributeAddBillingFragment();

}
