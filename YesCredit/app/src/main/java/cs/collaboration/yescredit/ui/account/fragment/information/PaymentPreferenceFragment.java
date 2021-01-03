package cs.collaboration.yescredit.ui.account.fragment.information;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.FragmentPaymentPreferenceBinding;
import cs.collaboration.yescredit.model.Card;
import cs.collaboration.yescredit.ui.account.Hostable;
import cs.collaboration.yescredit.viewmodel.ViewModelProviderFactory;
import dagger.android.support.DaggerFragment;

public class PaymentPreferenceFragment extends DaggerFragment {

    private static final String TAG = "PaymentPreferenceFragme";

    @Inject
    ViewModelProviderFactory providerFactory;

    private FragmentPaymentPreferenceBinding binding;
    private PaymentPreferenceViewModel viewModel;
    private Activity activity;
    private Hostable hostable;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPaymentPreferenceBinding.inflate(inflater);
        viewModel = new ViewModelProvider(this, providerFactory).get(PaymentPreferenceViewModel.class);
        initialization();
        subscribeObservers();
        return binding.getRoot();
    }

    private void subscribeObservers() {
        viewModel.observedCard().removeObservers(getViewLifecycleOwner());
        viewModel.observedCard().observe(getViewLifecycleOwner(), card -> {
            if (card != null) {
                binding.fragmentPaymentPrefCardName.setText(card.getCard_name());
                setFourDigitNumber(card);
            }
        });
    }

    private void initialization() {
        binding.fragmentPaymentPrefCardName.setText(R.string.no_available_label);

        binding.fragmentPaymentPrefCard.setOnClickListener(v -> {
            if (!binding.fragmentPaymentPrefCardName.getText().toString().equals("No Available")) {
                hostable.onInflate(v, getString(R.string.tag_fragment_payment_preference));
            }
        });
    }

    private void setFourDigitNumber(Card card) {
        String number = card.getCard_number();
        if (!number.isEmpty()) {
            number = "Credit card ●●●●" + number.substring(number.length() - 4);
            binding.fragmentPaymentPrefCardNumber.setText(number);
        }
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
        viewModel.getUserCard();
    }

}