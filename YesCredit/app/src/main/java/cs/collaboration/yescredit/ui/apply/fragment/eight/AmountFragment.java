package cs.collaboration.yescredit.ui.apply.fragment.eight;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.FragmentAmountBinding;
import cs.collaboration.yescredit.ui.apply.Hostable;
import cs.collaboration.yescredit.ui.apply.model.LoanForm;
import cs.collaboration.yescredit.viewmodel.ViewModelProviderFactory;
import dagger.android.support.DaggerFragment;

public class AmountFragment extends DaggerFragment {

    private static final String TAG = "AmountFragment";

    @Inject
    ViewModelProviderFactory providerFactory;

    private FragmentAmountBinding binding;
    private AmountViewModel viewModel;
    private LoanForm loanForm;
    private Hostable hostable;

    private String selectedLoanAmount;
    private String selectedLoanAmountLimit;
    private boolean isUserQualifiedToLoan;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAmountBinding.inflate(inflater);
        viewModel = new ViewModelProvider(this, providerFactory).get(AmountViewModel.class);
        subscribeObservers();
        navigation();
        return binding.getRoot();
    }

    private void subscribeObservers() {
        viewModel.observedLoanForm().removeObservers(getViewLifecycleOwner());
        viewModel.observedLoanForm().observe(getViewLifecycleOwner(), loanForm -> {
            if (loanForm != null) {
                AmountFragment.this.loanForm = loanForm;
                selectedLoanAmountLimit = loanForm.getLimit();
            }
        });
    }

    private void navigation() {

        String[] available_amount_array = requireActivity().getResources().getStringArray(R.array.available_amount_array_data);

        ArrayAdapter<CharSequence> adapter;

        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.available_amount_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.fragmentAmountEntered.setAdapter(adapter);
        binding.fragmentAmountEntered.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemSelected: " + available_amount_array[position]);
                isUserQualifiedToLoan = isQualifiedAmount(available_amount_array[position], selectedLoanAmountLimit);
                selectedLoanAmount = available_amount_array[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.fragmentAmountConfirm.setOnClickListener(v -> {

            if (isUserQualifiedToLoan) {
                enlistUserLoanInformation();
                hostable.onInflate(v, getString(R.string.tag_fragment_approved));
            } else {
                Snackbar.make(v, "Not Qualified!", Snackbar.LENGTH_SHORT).show();
            }
        });

    }

    private boolean isQualifiedAmount(String selected, String limit) {
        int amount = Integer.parseInt(selected);
        int limit_amount = Integer.parseInt(limit);
        return (amount <= limit_amount);
    }

    private void enlistUserLoanInformation() {
        loanForm.setRepayment_loan(selectedLoanAmount);
        hostable.onEnlist(loanForm);
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