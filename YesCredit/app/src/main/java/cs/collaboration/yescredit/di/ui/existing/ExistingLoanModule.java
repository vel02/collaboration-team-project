package cs.collaboration.yescredit.di.ui.existing;

import androidx.databinding.DataBindingUtil;

import javax.inject.Named;

import cs.collaboration.yescredit.databinding.ActivityExistingLoanBinding;
import cs.collaboration.yescredit.ui.existing.ExistingLoanActivity;
import dagger.Module;
import dagger.Provides;

@Module
public class ExistingLoanModule {

    @Provides
    static ActivityExistingLoanBinding provideActivityExistingBinding(ExistingLoanActivity activity, @Named("Existing Loan Activity") int layout) {
        return DataBindingUtil.setContentView(activity, layout);
    }

}
