package cs.collaboration.yescredit.di.ui.account;

import cs.collaboration.yescredit.di.ui.account.information.InformationScope;
import cs.collaboration.yescredit.di.ui.account.information.InformationViewModelModule;
import cs.collaboration.yescredit.di.ui.account.information.card.CardAccountScope;
import cs.collaboration.yescredit.di.ui.account.information.card.CardAccountViewModelModule;
import cs.collaboration.yescredit.di.ui.account.information.personal.PersonalScope;
import cs.collaboration.yescredit.di.ui.account.information.personal.PersonalViewModelModule;
import cs.collaboration.yescredit.di.ui.account.information.personal.address.AddAddressScope;
import cs.collaboration.yescredit.di.ui.account.information.personal.address.AddAddressViewModelModule;
import cs.collaboration.yescredit.di.ui.account.information.personal.addresses.AddressesScope;
import cs.collaboration.yescredit.di.ui.account.information.personal.addresses.AddressesViewModelModule;
import cs.collaboration.yescredit.di.ui.account.information.personal.phone.PhoneNumberScope;
import cs.collaboration.yescredit.di.ui.account.information.preference.PaymentPreferenceScope;
import cs.collaboration.yescredit.di.ui.account.information.preference.PaymentPreferenceViewModelModule;
import cs.collaboration.yescredit.ui.account.fragment.InformationFragment;
import cs.collaboration.yescredit.ui.account.fragment.information.CardAccountFragment;
import cs.collaboration.yescredit.ui.account.fragment.information.PaymentPreferenceFragment;
import cs.collaboration.yescredit.ui.account.fragment.information.PersonalFragment;
import cs.collaboration.yescredit.ui.account.fragment.information.card.AddBillingFragment;
import cs.collaboration.yescredit.ui.account.fragment.information.card.AddCardFragment;
import cs.collaboration.yescredit.ui.account.fragment.information.card.BillingAddressFragment;
import cs.collaboration.yescredit.ui.account.fragment.information.card.EditCardFragment;
import cs.collaboration.yescredit.ui.account.fragment.information.card.ViewCardFragment;
import cs.collaboration.yescredit.ui.account.fragment.information.personal.AddressesFragment;
import cs.collaboration.yescredit.ui.account.fragment.information.personal.PhoneNumberFragment;
import cs.collaboration.yescredit.ui.account.fragment.information.personal.addresses.AddAddressFragment;
import cs.collaboration.yescredit.ui.account.fragment.information.preference.PaymentMethodFragment;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class AccountFragmentBuilderModule {

    @InformationScope
    @ContributesAndroidInjector(
            modules = {
                    InformationViewModelModule.class
            }
    )
    abstract InformationFragment contributeInformationFragment();

    @PersonalScope
    @ContributesAndroidInjector(
            modules = {
                    PersonalViewModelModule.class
            }
    )
    abstract PersonalFragment contributePersonalFragment();

    @CardAccountScope
    @ContributesAndroidInjector(
            modules = {
                    CardAccountViewModelModule.class
            }
    )
    abstract CardAccountFragment contributeCardAccountFragment();

    @ContributesAndroidInjector
    abstract AddCardFragment contributeAddCardFragment();

    @ContributesAndroidInjector
    abstract BillingAddressFragment contributeBillingAddressFragment();

    @ContributesAndroidInjector
    abstract AddBillingFragment contributeAddBillingFragment();

    @PaymentPreferenceScope
    @ContributesAndroidInjector(
            modules = {
                    PaymentPreferenceViewModelModule.class
            }
    )
    abstract PaymentPreferenceFragment contributePaymentPreferenceFragment();

    @PhoneNumberScope
    @ContributesAndroidInjector
    abstract PhoneNumberFragment contributePhoneNumberFragment();

    @AddressesScope
    @ContributesAndroidInjector(
            modules = {
                    AddressesViewModelModule.class
            }
    )
    abstract AddressesFragment contributeAddressesFragment();

    @AddAddressScope
    @ContributesAndroidInjector(
            modules = {
                    AddAddressViewModelModule.class
            }
    )
    abstract AddAddressFragment contributeAddAddressFragment();

    @ContributesAndroidInjector
    abstract ViewCardFragment contributeViewCardFragment();

    @ContributesAndroidInjector
    abstract EditCardFragment contributeEditCardFragment();

    @ContributesAndroidInjector
    abstract PaymentMethodFragment contributePaymentMethodFragment();

}
