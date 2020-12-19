package cs.collaboration.yescredit.di.ui.account;

import cs.collaboration.yescredit.ui.account.fragment.information.CardAccountFragment;
import cs.collaboration.yescredit.ui.account.fragment.InformationFragment;
import cs.collaboration.yescredit.ui.account.fragment.information.PersonalFragment;
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

}
