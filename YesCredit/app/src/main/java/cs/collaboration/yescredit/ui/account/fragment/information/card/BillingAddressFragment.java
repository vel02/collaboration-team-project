package cs.collaboration.yescredit.ui.account.fragment.information.card;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.FragmentBillingAddressBinding;
import cs.collaboration.yescredit.ui.account.Hostable;
import dagger.android.support.DaggerFragment;

public class BillingAddressFragment extends DaggerFragment {

    private static final String TAG = "BillingAddressFragment";

    private FragmentBillingAddressBinding binding;
    private Activity activity;
    private Hostable hostable;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBillingAddressBinding.inflate(inflater);
        navigation();
        return binding.getRoot();
    }

    private void navigation() {
        binding.fragmentBillingAddressAdd.setOnClickListener(v -> {
            hostable.onInflate(v, getString(R.string.tag_fragment_add_billing_address));
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = getActivity();
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

    @Override
    public void onResume() {
        super.onResume();
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(activity.getResources().getColor(R.color.account_base));
    }
}