package cs.collaboration.yescredit.ui.apply.fragment.eleven;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import cs.collaboration.yescredit.databinding.FragmentReceiptBinding;
import cs.collaboration.yescredit.ui.apply.Hostable;
import cs.collaboration.yescredit.ui.apply.model.LoanForm;
import cs.collaboration.yescredit.ui.apply.model.UserForm;
import cs.collaboration.yescredit.viewmodel.ViewModelProviderFactory;
import dagger.android.support.DaggerFragment;

import static cs.collaboration.yescredit.util.Utility.currencyFormatterWithFixDecimal;

public class ReceiptFragment extends DaggerFragment {

    @Inject
    ViewModelProviderFactory providerFactory;

    private FragmentReceiptBinding binding;
    private ReceiptViewModel viewModel;
    private LoanForm loanForm;
    private Hostable hostable;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReceiptBinding.inflate(inflater);
        viewModel = new ViewModelProvider(this, providerFactory).get(ReceiptViewModel.class);
        subscribeObservers();
        navigation();
        return binding.getRoot();
    }

    private void subscribeObservers() {
        viewModel.observedLoanForm().removeObservers(getViewLifecycleOwner());
        viewModel.observedLoanForm().observe(getViewLifecycleOwner(), loanForm -> {
            if (loanForm != null) {
                ReceiptFragment.this.loanForm = loanForm;

                String displayInterest = loanForm.getRepayment_interest_used() + "%";
                String displayTax = loanForm.getRepayment_days().equals("30") ? "0.8%" : "0.6%";

                String date = getSchedule(getCurrentDate(), Integer.parseInt(loanForm.getRepayment_days()));
                String days = loanForm.getRepayment_days() + " Days to Pay";
                String loan = "PHP " + currencyFormatterWithFixDecimal(loanForm.getRepayment_loan());
                String interest = "PHP " + currencyFormatterWithFixDecimal(loanForm.getRepayment_interest()) + "(" + displayInterest + ")";
                String tax = "PHP " + currencyFormatterWithFixDecimal(loanForm.getRepayment_tax()) + "(" + displayTax + ")";
                String total = "PHP " + currencyFormatterWithFixDecimal(loanForm.getRepayment_total());
                String penalty = "PHP " + currencyFormatterWithFixDecimal(loanForm.getRepayment_penalty());

                binding.fragmentReceiptDaysToPay.setText(days);
                binding.fragmentReceiptDate.setText(date);
                binding.fragmentReceiptLoan.setText(loan);
                binding.fragmentReceiptInterest.setText(interest);
                binding.fragmentReceiptTax.setText(tax);
                binding.fragmentReceiptTotal.setText(total);
                binding.fragmentReceiptPenalty.setText(penalty);
            }
        });

        viewModel.observedEnlistNotification().removeObservers(getViewLifecycleOwner());
        viewModel.observedEnlistNotification().observe(getViewLifecycleOwner(), enlistNotification -> {
            if (enlistNotification != null) {
                switch (enlistNotification) {
                    case SUCCESS:
                        Toast.makeText(requireContext(), "Transaction Success!", Toast.LENGTH_SHORT).show();
                        break;
                    case FAILED:
                        Toast.makeText(requireContext(), "Transaction Failed!", Toast.LENGTH_SHORT).show();
                        break;
                }
                requireActivity().finish();
                reset();
            }
        });

    }

    private void navigation() {

        binding.fragmentReceiptAccept.setOnClickListener(v -> {
            viewModel.searchAvailableBenefits();
            viewModel.enlistUserLoanInformation(loanForm);
        });

    }

    private void reset() {
        LoanForm loanForm = new LoanForm();
        UserForm userForm = new UserForm();
        hostable.onEnlist(loanForm);
        hostable.onEnlist(userForm);
    }

    private String getCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return df.format(c);
    }

    public static String getSchedule(String startDate, int numberOfDays) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(dateFormat.parse(startDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DAY_OF_WEEK, numberOfDays);
        return dateFormat.format(c.getTime());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = getActivity();
        if (!(activity instanceof Hostable)) {
            assert activity != null;
            throw new ClassCastException(activity.getClass().getSimpleName()
                    + " must implement Hostable interface.");
        }
        hostable = (Hostable) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        hostable = null;
    }
}