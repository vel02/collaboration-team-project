package cs.collaboration.yescredit.ui.account.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Objects;

import javax.inject.Inject;

import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.FragmentInformationBinding;
import cs.collaboration.yescredit.ui.account.Hostable;
import cs.collaboration.yescredit.viewmodel.ViewModelProviderFactory;
import dagger.android.support.DaggerFragment;

public class InformationFragment extends DaggerFragment {

    private static final String TAG = "PersonalInformationFrag";

    @Inject
    ViewModelProviderFactory providerFactory;

    private FragmentInformationBinding binding;
    private InformationViewModel viewModel;
    private Activity activity;
    private Hostable hostable;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentInformationBinding.inflate(inflater);
        viewModel = new ViewModelProvider(this, providerFactory).get(InformationViewModel.class);
        subscribeObservers();
        navigation();
        return binding.getRoot();
    }

    private void subscribeObservers() {

        viewModel.observedUserInformation().removeObservers(getViewLifecycleOwner());
        viewModel.observedUserInformation().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                ImageLoader.getInstance().displayImage(user.getProfile_image(), binding.fragmentInformationImage);
                binding.fragmentInformationName.setText(nameFormatter(user.getFirst_name(), user.getLast_name()));
            }
        });

        viewModel.observedUserEmail().removeObservers(getViewLifecycleOwner());
        viewModel.observedUserEmail().observe(getViewLifecycleOwner(), email -> {
            if (email != null || !email.isEmpty()) {
                binding.fragmentInformationEmail.setText(emailFormatter(Objects.requireNonNull(email)));
            }
        });

    }

    private String nameFormatter(String first_name, String last_name) {
        return first_name + " " + last_name;
    }

    private String emailFormatter(String email) {
        return "@" + email.substring(0, email.indexOf("@"));
    }

    private void navigation() {

        binding.fragmentInformationOut.setOnClickListener(v -> hostable.onLogout());

        binding.fragmentInformationInfo.setOnClickListener(v -> {
            hostable.onInflate(v, getString(R.string.tag_fragment_personal));
        });

        binding.fragmentInformationCards.setOnClickListener(v -> {
            hostable.onInflate(v, getString(R.string.tag_fragment_card));
        });

        binding.fragmentInformationPreference.setOnClickListener(v -> {
            hostable.onInflate(v, getString(R.string.tag_fragment_preference));
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
        toolbar.setBackgroundColor(activity.getResources().getColor(R.color.account_base));
    }
}