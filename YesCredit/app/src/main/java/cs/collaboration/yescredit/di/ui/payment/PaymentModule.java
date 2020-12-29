package cs.collaboration.yescredit.di.ui.payment;

import androidx.databinding.DataBindingUtil;

import javax.inject.Named;

import cs.collaboration.yescredit.databinding.ActivityPaymentBinding;
import cs.collaboration.yescredit.ui.payment.PaymentActivity;
import dagger.Module;
import dagger.Provides;

@Module
public class PaymentModule {

    @Provides
    static ActivityPaymentBinding provideActivityPaymentBinding(PaymentActivity activity, @Named("Payment Activity") int layout) {
        return DataBindingUtil.setContentView(activity, layout);
    }

}
