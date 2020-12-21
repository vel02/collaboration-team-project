package cs.collaboration.yescredit.ui.account.fragment.information.personal;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.FragmentPhoneNumberBinding;
import cs.collaboration.yescredit.ui.account.Hostable;
import dagger.android.support.DaggerFragment;

public class PhoneNumberFragment extends DaggerFragment {

    private static final String TAG = "PhoneNumberFragment";

    private FragmentPhoneNumberBinding binding;
    private Activity activity;
    private Hostable hostable;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPhoneNumberBinding.inflate(inflater);
        navigation();
        return binding.getRoot();
    }

    private void navigation() {

        binding.fragmentPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s == null) return;
                binding.fragmentPhoneAdd.setEnabled(!s.toString().isEmpty());
            }
        });

        binding.fragmentPhoneAdd.setOnClickListener(v -> {
            //save to firebase
        });
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
        toolbar.setBackgroundColor(activity.getResources().getColor(R.color.white));
    }
}