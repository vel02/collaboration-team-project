package cs.collaboration.yescredit.ui.apply.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import cs.collaboration.yescredit.databinding.FragmentReceiptBinding;
import cs.collaboration.yescredit.model.Loan;
import cs.collaboration.yescredit.ui.apply.Hostable;
import cs.collaboration.yescredit.ui.apply.SessionManager;
import cs.collaboration.yescredit.ui.apply.model.LoanForm;
import cs.collaboration.yescredit.ui.apply.model.UserForm;
import cs.collaboration.yescredit.util.Keys;
import dagger.android.support.DaggerFragment;

import static cs.collaboration.yescredit.util.Utility.currencyFormatterWithFixDecimal;

public class ReceiptFragment extends DaggerFragment {

    private static final String TAG = "ReceiptFragment";


    @Inject
    SessionManager sessionManager;

    private FragmentReceiptBinding binding;
    private LoanForm loanForm;
    private Hostable hostable;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReceiptBinding.inflate(inflater);
        getLoanInfo();
        navigation();
        return binding.getRoot();
    }

    private void getLoanInfo() {
        sessionManager.observeLoanForm().removeObservers(getViewLifecycleOwner());
        sessionManager.observeLoanForm().observe(getViewLifecycleOwner(), loanForm -> {
            if (loanForm != null) {

                String displayInterest = loanForm.getRepayment_days().equals("30") ? "15%" : "12%";
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

                ReceiptFragment.this.loanForm = loanForm;

                sessionManager.observeLoanForm().removeObservers(getViewLifecycleOwner());
            }
        });


    }

    private void navigation() {

        binding.fragmentReceiptAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEnlistLoan(v);
            }
        });

    }

    private void onEnlistLoan(View view) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        FirebaseUser current = FirebaseAuth.getInstance().getCurrentUser();

        String loanId = reference.child(Keys.DATABASE_NODE_LOAN)
                .push().getKey();

        if (current != null && loanId != null) {
            Loan loan = new Loan();
            loan.setUserId(current.getUid());
            loan.setLoanId(loanId);
            loan.setLevelOfEducation(loanForm.getLevelOfEducation());
            loan.setReason(loanForm.getReason());
            loan.setMoreDetails(loanForm.getMoreDetails());
            loan.setOutstanding(loanForm.getOutstanding());
            loan.setCivilStatus(loanForm.getCivilStatus());
            loan.setSourceOfIncome(loanForm.getSourceOfIncome());
            loan.setIncomePerMonth(loanForm.getIncomePerMonth());
            loan.setLimit(loanForm.getLimit());
            loan.setStatus(loanForm.getStatus());
            loan.setRepayment_loan(loanForm.getRepayment_loan());
            loan.setRepayment_date(getSchedule(getCurrentDate(), Integer.parseInt(loanForm.getRepayment_days())));
            loan.setRepayment_interest(loanForm.getRepayment_interest());
            loan.setRepayment_tax(loanForm.getRepayment_tax());
            loan.setRepayment_penalty(loanForm.getRepayment_penalty());
            loan.setRepayment_total(loanForm.getRepayment_total());
            loan.setRepayment_days(loanForm.getRepayment_days());

            reference.child(Keys.DATABASE_NODE_LOAN)
                    .child(loanId).setValue(loan)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Snackbar.make(view, "Transaction Success!", Snackbar.LENGTH_SHORT).show();
                        } else
                            Snackbar.make(view, "Transaction Failed!", Snackbar.LENGTH_SHORT).show();
                    });

            requireActivity().finish();
            reset();

        }

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