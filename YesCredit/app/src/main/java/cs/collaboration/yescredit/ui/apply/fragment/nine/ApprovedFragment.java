package cs.collaboration.yescredit.ui.apply.fragment.nine;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.FragmentApprovedBinding;
import cs.collaboration.yescredit.ui.apply.Hostable;
import cs.collaboration.yescredit.viewmodel.ViewModelProviderFactory;
import dagger.android.support.DaggerFragment;

import static cs.collaboration.yescredit.util.Utility.currencyFormatter;


public class ApprovedFragment extends DaggerFragment {

    private static final String TAG = "ApprovedFragment";

    @Inject
    ViewModelProviderFactory providerFactory;

    private FragmentApprovedBinding binding;
    private ApprovedViewModel viewModel;
    private Hostable hostable;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentApprovedBinding.inflate(inflater);
        viewModel = new ViewModelProvider(this, providerFactory).get(ApprovedViewModel.class);
        subscribeObservers();
        navigation();
        return binding.getRoot();
    }

    private void subscribeObservers() {
        viewModel.observedLoanForm().removeObservers(getViewLifecycleOwner());
        viewModel.observedLoanForm().observe(getViewLifecycleOwner(), loanForm -> {
            if (loanForm != null) {
                String amount = "PHP " + currencyFormatter(loanForm.getRepayment_loan());
                binding.fragmentApprovedAmount.setText(amount);
            }
        });
        binding.fragmentApprovedCancel.setOnClickListener(v -> requireActivity().finish());
    }

    private void navigation() {

        binding.fragmentApprovedChoose.setOnClickListener(v -> hostable.onInflate(v, getString(R.string.tag_fragment_schedule)));

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