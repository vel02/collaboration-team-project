package cs.collaboration.yescredit.ui.apply.fragment.seven;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.FragmentSubmitBinding;
import cs.collaboration.yescredit.ui.apply.Hostable;
import cs.collaboration.yescredit.ui.apply.model.LoanForm;
import cs.collaboration.yescredit.viewmodel.ViewModelProviderFactory;
import dagger.android.support.DaggerFragment;

import static cs.collaboration.yescredit.util.Utility.currencyFormatter;

/*
                   this will increase depends on number of times users paid loans
                   2 = 5,000 or 10,000
                   5 = 20,000
                   10 = 30,000
                   20 = 50,000 which is maximum amount
                   20+ = 50,000 fix

                   logic:
                   number of times = get current number of paid loans in database.
                */
public class SubmitFragment extends DaggerFragment {

    private static final String TAG = "SubmitFragment";

    @Inject
    ViewModelProviderFactory providerFactory;

    private FragmentSubmitBinding binding;
    private SubmitViewModel viewModel;
    private LoanForm loanForm;
    private Hostable hostable;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSubmitBinding.inflate(inflater);
        viewModel = new ViewModelProvider(this, providerFactory).get(SubmitViewModel.class);
        subscribeObservers();
        navigation();
        return binding.getRoot();
    }

    private void subscribeObservers() {
        viewModel.observedLoanForm().removeObservers(getViewLifecycleOwner());
        viewModel.observedLoanForm().observe(getViewLifecycleOwner(), loanForm -> {
            if (loanForm != null) {
                SubmitFragment.this.loanForm = loanForm;
                String display = loanForm.getLevelOfEducation() + "\n" + loanForm.getReason() + "\n\n"
                        + "Reason: " + loanForm.getMoreDetails() + "\nOutstanding Loans: "
                        + loanForm.getOutstanding() + "\n" + "Civil Status: " + loanForm.getCivilStatus()
                        + "\n\n" + "Source of Income: " + loanForm.getSourceOfIncome() + "\n"
                        + "Household Income: " + currencyFormatter(loanForm.getIncomePerMonth()) + " PHP";
                binding.fragmentSubmitLoanInfo.setText(display);
            }
        });
    }

    private void navigation() {

        binding.fragmentSubmitApplication.setOnClickListener(v -> {
            loanForm.setLimit(getLimitAmount((int) viewModel.getNumberOfPaid()));
            loanForm.setStatus("on-going"); //Paid, On-Going, Unpaid
            hostable.onEnlist(loanForm);
            hostable.onInflate(v, getString(R.string.tag_fragment_amount_application));
            viewModel.setNumberOfPaid(0);
        });
    }

    private String getLimitAmount(int number_of_times) {
        if (number_of_times >= 2 && number_of_times < 5) {
            return "10000";
        } else if (number_of_times >= 5 && number_of_times < 10) {
            return "20000";
        } else if (number_of_times >= 10 && number_of_times < 20) {
            return "30000";
        } else if (number_of_times >= 20) {
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