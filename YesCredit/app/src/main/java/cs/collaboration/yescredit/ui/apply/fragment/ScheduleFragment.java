package cs.collaboration.yescredit.ui.apply.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import javax.inject.Inject;

import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.FragmentScheduleBinding;
import cs.collaboration.yescredit.model.Code;
import cs.collaboration.yescredit.ui.apply.Hostable;
import cs.collaboration.yescredit.ui.apply.SessionManager;
import cs.collaboration.yescredit.ui.apply.model.LoanForm;
import cs.collaboration.yescredit.util.Keys;
import dagger.android.support.DaggerFragment;

import static cs.collaboration.yescredit.util.Utility.currencyFormatterWithFixDecimal;

public class ScheduleFragment extends DaggerFragment {

    private static final String TAG = "ScheduleFragment";

    @Inject
    SessionManager sessionManager;

    private FragmentScheduleBinding binding;
    private LoanForm loanForm;
    private Hostable hostable;

    private int benefit;
    private double interest30;
    private double interest14;
    private double penalty30;
    private double penalty14;
    private double total30;
    private double total14;
    private double tax30;
    private double tax14;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentScheduleBinding.inflate(inflater);
        getLoanInfo();
        searchAvailableBenefits();
        navigation();
        return binding.getRoot();
    }

    private void getLoanInfo() {
        sessionManager.observeLoanForm().removeObservers(getViewLifecycleOwner());
        sessionManager.observeLoanForm().observe(getViewLifecycleOwner(), new Observer<LoanForm>() {
            @Override
            public void onChanged(LoanForm loanForm) {
                if (loanForm != null) {
                    Log.d(TAG, "onChanged: " + loanForm.getRepayment_loan());
                    displayDueDate(loanForm.getRepayment_loan());
                    ScheduleFragment.this.loanForm = loanForm;
                }
            }
        });
    }

    private void displayDueDate(String value) {
        double loan = Double.parseDouble(value);
        Log.d(TAG, "computeDueDate: loan: " + loan);

        compute30DaysPayment(loan);
        compute14DaysPayment(loan);
    }

    private void compute30DaysPayment(double loan) {

        double benefit = Double.parseDouble("0.0" + this.benefit);
        double interest = (0.15 - benefit);
        final double tax = 0.008;

        // 30 days to pay
        double service = getServiceFee(loan, interest, tax);
        double total = getTotal(loan, service);
        setRepaymentInfo("30_days", total, getTax(loan, tax), (loan * interest), (loan * 0.08));

        String display_due = "PHP " + currencyFormatterWithFixDecimal(String.valueOf(total)) + " due (Date)";
        String display_service = "Service Fee = PHP " + currencyFormatterWithFixDecimal(String.valueOf(service)) + "(" + (15 - this.benefit) + "% Interest + 0.8% tax)";
        binding.fragmentScheduleAmount30.setText(display_due);
        binding.fragmentScheduleService30.setText(display_service);
    }

    private void compute14DaysPayment(double loan) {

        double benefit = Double.parseDouble("0.0" + this.benefit);
        double interest = (0.12 - benefit);
        final double tax = 0.006;

        double service = getServiceFee(loan, interest, tax);
        double total = getTotal(loan, service);
        setRepaymentInfo("14_days", total, getTax(loan, tax), (loan * interest), (loan * 0.08));

        String display_due = "PHP " + currencyFormatterWithFixDecimal(String.valueOf(total)) + " due (Date)";
        String display_service = "Service Fee = PHP " + currencyFormatterWithFixDecimal(String.valueOf(service)) + "(" + (12 - this.benefit) + "% Interest + 0.6% tax)";
        binding.fragmentScheduleAmount14.setText(display_due);
        binding.fragmentScheduleService14.setText(display_service);
    }

    private void setRepaymentInfo(String flag, double total, double tax, double interest, double penalty) {
        if (flag.equals("30_days")) {
            this.total30 = total;
            this.tax30 = tax;
            this.interest30 = interest;
            this.penalty30 = penalty;
        } else {
            this.total14 = total;
            this.tax14 = tax;
            this.interest14 = interest;
            this.penalty14 = penalty;
        }
    }

    private void searchAvailableBenefits() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser current = FirebaseAuth.getInstance().getCurrentUser();

        if (current != null) {

            Query query = reference.child(Keys.DATABASE_NODE_CODES)
                    .orderByChild(Keys.DATABASE_FIELD_FROM_ID)
                    .equalTo(current.getUid());

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<Code> available = new ArrayList<>();
                    long number_of_codes = 0;
                    for (DataSnapshot singleShot : snapshot.getChildren()) {
                        number_of_codes++;

                        Code generated = singleShot.getValue(Code.class);

                        if (generated != null) {
                            if (generated.getReferred_status().equals("referred")
                                    && generated.getCode_status().equals("not-used")) {
                                available.add(generated);
                                if (number_of_codes == snapshot.getChildrenCount()) {
                                    setBenefits(available);
                                }
                            }
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void setBenefits(ArrayList<Code> available) {

        if (!available.isEmpty()) {
            if (binding.fragmentScheduleBenefitsRoot.getVisibility() == View.GONE)
                binding.fragmentScheduleBenefitsRoot.setVisibility(View.VISIBLE);
        }

        String[] available_benefit_array = new String[available.size()];
        String[] benefit_array = new String[available.size()];

        for (int i = 0; i < available.size(); i++) {
            available_benefit_array[i] = available.get(i).getCode();
            benefit_array[i] = available.get(i).getCode_benefit();
        }

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, available_benefit_array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.fragmentScheduleBenefits.setAdapter(adapter);
        binding.fragmentScheduleBenefits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemSelected: " + benefit_array[position]);
                benefit = Integer.parseInt(benefit_array[position]);
                displayDueDate(loanForm.getRepayment_loan());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void navigation() {

        binding.fragmentScheduleGroup.setOnCheckedChangeListener((group, checkedId) -> {
            int selected = group.getCheckedRadioButtonId();

            RadioButton button = binding.getRoot().findViewById(selected);
            String days = button.getText().toString();
            loanForm.setRepayment_days(getDaysToPay(days));

            if (checkedId == binding.fragmentSchedule30Days.getId()) {
                loanForm.setRepayment_total(String.valueOf(total30));
                loanForm.setRepayment_tax(String.valueOf(tax30));
                loanForm.setRepayment_interest(String.valueOf(interest30));
                loanForm.setRepayment_penalty(String.valueOf(penalty30));
                loanForm.setRepayment_interest_used(String.valueOf((15 - this.benefit)));
            } else if (checkedId == binding.fragmentSchedule14Days.getId()) {
                loanForm.setRepayment_total(String.valueOf(total14));
                loanForm.setRepayment_tax(String.valueOf(tax14));
                loanForm.setRepayment_interest(String.valueOf(interest14));
                loanForm.setRepayment_penalty(String.valueOf(penalty14));
                loanForm.setRepayment_interest_used(String.valueOf((12 - this.benefit)));
            }

            Log.d(TAG, "onCheckedChanged: updated: " + loanForm);
        });

        binding.fragmentScheduleConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hostable.onEnlist(loanForm);
                hostable.onInflate(v, getString(R.string.tag_fragment_receipt));
            }
        });
    }

    private String getDaysToPay(String value) {
        return value.substring(0, value.indexOf(" "));
    }

    private double getTax(double loan, double tax) {
        return loan * tax;
    }

    private double getServiceFee(double loan, double interest, double tax) {
        double due = loan * interest;
        return due + getTax(loan, tax);
    }

    private double getTotal(double loan, double service) {
        return loan + service;
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