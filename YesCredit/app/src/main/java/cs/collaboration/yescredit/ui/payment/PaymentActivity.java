package cs.collaboration.yescredit.ui.payment;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.nostra13.universalimageloader.core.ImageLoader;

import javax.inject.Inject;

import cs.collaboration.yescredit.BaseActivity;
import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.ActivityPaymentBinding;
import cs.collaboration.yescredit.model.Card;
import cs.collaboration.yescredit.viewmodel.ViewModelProviderFactory;

import static cs.collaboration.yescredit.util.Utility.currencyFormatterWithFixDecimal;

public class PaymentActivity extends BaseActivity {

    private static final String TAG = "PaymentActivity";

    @Inject
    ActivityPaymentBinding binding;

    @Inject
    ViewModelProviderFactory providerFactory;

    private PaymentViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, providerFactory).get(PaymentViewModel.class);
        viewModel.showAvailableTransaction();
        initialization();
        subscribeObservers();
    }

    private void initialization() {

        binding.contentPayment.contentPaymentPay.setOnClickListener(v -> {
            if (viewModel.getShow() && !viewModel.getConfirmed()) {
                Toast.makeText(this, "Please, confirm your card before payment", Toast.LENGTH_SHORT).show();
            }

            if (!viewModel.getShow()) {
                viewModel.setShow(true);
                viewModel.getUserPrimaryCard();
                binding.contentPayment.fragmentPaymentCreditRoot.setVisibility(View.VISIBLE);
            }

            if (viewModel.getConfirmed()) {
                viewModel.updateUserTransaction();
            }
        });

        binding.contentPayment.fragmentPaymentCreditConfirm.setOnClickListener(v -> {
            viewModel.setConfirmed(true);
            setConfirmAttributes();
        });

    }

    private void setConfirmAttributes() {
        binding.contentPayment.fragmentPaymentCreditConfirm.setText(R.string.confirmed_label);
        binding.contentPayment.fragmentPaymentCreditConfirm.setTextColor(getResources().getColor(R.color.grey_one));
    }

    private void subscribeObservers() {
        viewModel.observedLoan().observe(this, loan -> {
            if (loan != null) {

                String days = loan.getRepayment_days() + " days to pay";
                String schedule = loan.getRepayment_date();
                String amount = "PHP " + currencyFormatterWithFixDecimal(loan.getRepayment_loan());
                String interest = "PHP " + currencyFormatterWithFixDecimal(loan.getRepayment_interest())
                        + "(" + loan.getRepayment_interest_used() + "%)";
                String tax = "PHP " + currencyFormatterWithFixDecimal(loan.getRepayment_tax())
                        + "(0.8%)";
                String total = "PHP " + currencyFormatterWithFixDecimal(loan.getRepayment_total());

                binding.contentPayment.contentPaymentDaysToPay.setText(days);
                binding.contentPayment.contentPaymentDate.setText(schedule);
                binding.contentPayment.contentPaymentLoan.setText(amount);
                binding.contentPayment.contentPaymentInterest.setText(interest);
                binding.contentPayment.contentPaymentTax.setText(tax);
                binding.contentPayment.contentPaymentTotal.setText(total);

            }
        });

        viewModel.observedState().observe(this, state -> {
            if (state != null) {
                switch (state) {
                    case AVAILABLE:
                        binding.contentPayment.contentPaymentReceiptRoot.setVisibility(View.VISIBLE);
                        binding.contentPayment.contentPaymentStatusRoot.setVisibility(View.GONE);
                        break;
                    case NOT_AVAILABLE:
                        binding.contentPayment.contentPaymentReceiptRoot.setVisibility(View.GONE);
                        binding.contentPayment.contentPaymentStatusRoot.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        viewModel.observedCard().observe(this, card -> {
            assert card != null;
            ImageLoader.getInstance().displayImage(card.getCard_image(), binding.contentPayment.fragmentPaymentCreditImage);
            binding.contentPayment.fragmentPaymentCardName.setText(card.getCard_name());
            setFourDigitNumber(card);
        });

        viewModel.observedComplete().observe(this, complete -> {
            assert complete != null;
            if (complete) {
                binding.contentPayment.contentPaymentReceiptRoot.setVisibility(View.GONE);
                binding.contentPayment.contentPaymentStatusRoot.setVisibility(View.VISIBLE);
                Toast.makeText(this, "Transaction Complete", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setFourDigitNumber(Card card) {
        String number = card.getCard_number();
        if (!number.isEmpty()) {
            number = "Credit card ●●●●" + number.substring(number.length() - 4);
            binding.contentPayment.fragmentPaymentCardNumber.setText(number);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkAuthenticationState();
        if (viewModel.getShow()) {
            binding.contentPayment.fragmentPaymentCreditRoot.setVisibility(View.VISIBLE);
        }

        if (viewModel.getConfirmed()) {
            setConfirmAttributes();
        }
    }
}