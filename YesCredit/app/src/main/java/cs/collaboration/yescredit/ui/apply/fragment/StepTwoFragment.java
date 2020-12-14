package cs.collaboration.yescredit.ui.apply.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cs.collaboration.yescredit.databinding.FragmentStepTwoBinding;
import cs.collaboration.yescredit.ui.apply.Hostable;
import dagger.android.support.DaggerFragment;

public class StepTwoFragment extends DaggerFragment {

    private static final String TAG = "StepTwoFragment";

    private FragmentStepTwoBinding binding;

    private Hostable hostable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStepTwoBinding.inflate(inflater);
        Log.d(TAG, "onCreateView: started");
        return binding.getRoot();
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