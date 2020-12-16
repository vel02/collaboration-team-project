package cs.collaboration.yescredit.ui.apply.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import javax.inject.Inject;

import cs.collaboration.yescredit.databinding.FragmentApprovedBinding;
import cs.collaboration.yescredit.ui.apply.SessionManager;
import dagger.android.support.DaggerFragment;

import static cs.collaboration.yescredit.util.Utility.currencyFormatter;


public class ApprovedFragment extends DaggerFragment {

    private static final String TAG = "ApprovedFragment";

    @Inject
    SessionManager sessionManager;

    private FragmentApprovedBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentApprovedBinding.inflate(inflater);
        getLoanInfo();
        return binding.getRoot();
    }

    private void getLoanInfo() {
        sessionManager.observeLoanForm().removeObservers(getViewLifecycleOwner());
        sessionManager.observeLoanForm().observe(getViewLifecycleOwner(), loanForm -> {
            if (loanForm != null) {
                String amount = "PHP " + currencyFormatter(loanForm.getRepayment_loan());
                binding.fragmentApprovedAmount.setText(amount);
            }
        });
        binding.fragmentApprovedCancel.setOnClickListener(v -> requireActivity().finish());
    }

}