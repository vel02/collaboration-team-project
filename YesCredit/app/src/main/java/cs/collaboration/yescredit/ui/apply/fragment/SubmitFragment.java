package cs.collaboration.yescredit.ui.apply.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import javax.inject.Inject;

import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.FragmentSubmitBinding;
import cs.collaboration.yescredit.model.Loan;
import cs.collaboration.yescredit.ui.apply.Hostable;
import cs.collaboration.yescredit.ui.apply.SessionManager;
import cs.collaboration.yescredit.ui.apply.model.LoanForm;
import dagger.android.support.DaggerFragment;

import static cs.collaboration.yescredit.util.Utility.currencyFormatter;


public class SubmitFragment extends DaggerFragment {

    private static final String TAG = "SubmitFragment";

    @Inject
    SessionManager sessionManager;

    private FragmentSubmitBinding binding;
    private Hostable hostable;

    private LoanForm loanForm;

    private long number_of_times;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSubmitBinding.inflate(inflater);
        getLoanInfo();
        navigation();
        return binding.getRoot();
    }

    private void getLoanInfo() {
        sessionManager.observeLoanForm().removeObservers(getViewLifecycleOwner());
        sessionManager.observeLoanForm().observe(getViewLifecycleOwner(), loanForm -> {
            if (loanForm != null) {
                Log.d(TAG, "getLoanInfo: loanForm: " + loanForm);
                String display = loanForm.getLevelOfEducation() + "\n" + loanForm.getReason() + "\n\n"
                        + "Reason: " + loanForm.getMoreDetails() + "\nOutstanding Loans: "
                        + loanForm.getOutstanding() + "\n" + "Civil Status: " + loanForm.getCivilStatus()
                        + "\n\n" + "Source of Income: " + loanForm.getSourceOfIncome() + "\n"
                        + "Household Income: " + currencyFormatter(loanForm.getIncomePerMonth()) + " PHP";
                binding.fragmentSubmitLoanInfo.setText(display);
                SubmitFragment.this.loanForm = loanForm;
            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child(getString(R.string.database_node_loans))
                .orderByChild(getString(R.string.database_field_user_id))
                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot singleShot : snapshot.getChildren()) {
                    Loan loan = singleShot.getValue(Loan.class);
                    if (loan != null) {
                        String status = loan.getStatus();
                        if (status.equals("paid")) {
                            number_of_times++;
                            Log.d(TAG, "onDataChange: " + number_of_times);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void navigation() {

        binding.fragmentSubmitApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoanForm loanForm = new LoanForm();
                loanForm.setLevelOfEducation(SubmitFragment.this.loanForm.getLevelOfEducation());
                loanForm.setReason(SubmitFragment.this.loanForm.getReason());
                loanForm.setMoreDetails(SubmitFragment.this.loanForm.getMoreDetails());
                loanForm.setOutstanding(SubmitFragment.this.loanForm.getOutstanding());
                loanForm.setCivilStatus(SubmitFragment.this.loanForm.getCivilStatus());
                loanForm.setSourceOfIncome(SubmitFragment.this.loanForm.getSourceOfIncome());
                loanForm.setIncomePerMonth(SubmitFragment.this.loanForm.getIncomePerMonth());

                    /*
                        this will increase depends on number of times users paid loans
                        1 = 1,000
                        2 = 2,000
                        5 = 5,000
                        10 = 10,000
                        15 = 20,000
                        20 = 30,000
                        40 = 50,000 which is maximum amount
                        41+ = 50,000 fix

                        logic:
                        number of times = get current number of paid loans in database.
                     */
                loanForm.setLimit(getLimitAmount((int) number_of_times));

                    /*
                        Paid, On-Going, Unpaid
                     */
                loanForm.setStatus("on-going");
                loanForm.setRepayment_loan("");
                loanForm.setRepayment_date("");
                loanForm.setRepayment_interest("");
                loanForm.setRepayment_penalty("");
                loanForm.setRepayment_total("");

                hostable.onEnlist(loanForm);
                hostable.onInflate(v, getString(R.string.tag_fragment_amount_application));
                number_of_times = 0;

            }
        });
    }

    private String getLimitAmount(int number_of_times) {
        if (number_of_times >= 2 && number_of_times < 5) {
            return "10000";
        } else if (number_of_times >= 5 && number_of_times < 10) {
            return "20000";
        } else if (number_of_times >= 10 && number_of_times < 25) {
            return "30000";
        } else if (number_of_times >= 25) {
            return "50000";
        }
        return "10000";
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