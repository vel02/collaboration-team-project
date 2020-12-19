package cs.collaboration.yescredit.ui.account.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.FragmentInformationBinding;
import cs.collaboration.yescredit.ui.account.Hostable;
import dagger.android.support.DaggerFragment;

public class InformationFragment extends DaggerFragment {

    private static final String TAG = "PersonalInformationFrag";

    private FragmentInformationBinding binding;

    private Hostable hostable;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentInformationBinding.inflate(inflater);
        navigation();
        return binding.getRoot();
    }

    private void navigation() {

        binding.fragmentInformationOut.setOnClickListener(v -> hostable.onLogout());

        binding.fragmentInformationInfo.setOnClickListener(v -> {
            hostable.onInflate(v, getString(R.string.tag_fragment_personal));
        });

        binding.fragmentInformationCards.setOnClickListener(v -> {
            hostable.onInflate(v, getString(R.string.tag_fragment_card));
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