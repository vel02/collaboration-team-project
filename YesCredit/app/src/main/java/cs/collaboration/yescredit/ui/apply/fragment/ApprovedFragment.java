package cs.collaboration.yescredit.ui.apply.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import javax.inject.Inject;

import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.FragmentApprovedBinding;
import cs.collaboration.yescredit.ui.apply.Hostable;
import cs.collaboration.yescredit.ui.apply.SessionManager;
import dagger.android.support.DaggerFragment;

import static cs.collaboration.yescredit.util.Utility.currencyFormatter;


public class ApprovedFragment extends DaggerFragment {

    private static final String TAG = "ApprovedFragment";

    @Inject
    SessionManager sessionManager;

    private FragmentApprovedBinding binding;
    private Hostable hostable;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentApprovedBinding.inflate(inflater);
        getLoanInfo();
        navigation();
        return binding.getRoot();
    }

    private void getLoanInfo() {
        sessionManager.observeLoanForm().removeObservers(getViewLifecycleOwner());
        sessionManager.observeLoanForm().observe(getViewLifecycleOwner(), loanForm -> {
            if (loanForm != null) {
                String amount = "PHP " + currencyFormatter(loanForm.getRepayment_loan());
                binding.fragmentApprovedAmount.setText(amount);
                Log.d(TAG, "getLoanInfo: " + loanForm);
            }
        });
        binding.fragmentApprovedCancel.setOnClickListener(v -> requireActivity().finish());
    }

    private void navigation() {

        binding.fragmentApprovedChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hostable.onInflate(v, getString(R.string.tag_fragment_schedule));
            }
        });

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