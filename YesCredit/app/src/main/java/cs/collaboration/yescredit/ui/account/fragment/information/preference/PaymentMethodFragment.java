package cs.collaboration.yescredit.ui.account.fragment.information.preference;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DatabaseReference;

import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.FragmentPaymentMethodBinding;
import cs.collaboration.yescredit.ui.account.Hostable;
import dagger.android.support.DaggerFragment;

public class PaymentMethodFragment extends DaggerFragment {

    private static final String TAG = "PaymentMethodFragment";


    private FragmentPaymentMethodBinding binding;
    private DatabaseReference reference;
    private Activity activity;
    private Hostable hostable;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPaymentMethodBinding.inflate(inflater);
        return binding.getRoot();
    }


    @Override
    public void onAttach(Context context) {
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