package cs.collaboration.yescredit.ui.account.fragment.information.personal;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import javax.inject.Inject;

import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.FragmentAddressesBinding;
import cs.collaboration.yescredit.ui.account.Hostable;
import cs.collaboration.yescredit.ui.account.adapter.addresses.AddressesRecyclerAdapter;
import cs.collaboration.yescredit.viewmodel.ViewModelProviderFactory;
import dagger.android.support.DaggerFragment;

public class AddressesFragment extends DaggerFragment {

    private static final String TAG = "AddressesFragment";

    @Inject
    ViewModelProviderFactory providerFactory;

    private FragmentAddressesBinding binding;
    private AddressesViewModel viewModel;
    private Activity activity;
    private Hostable hostable;

    private AddressesRecyclerAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddressesBinding.inflate(inflater);
        viewModel = new ViewModelProvider(this, providerFactory).get(AddressesViewModel.class);
        initialization();
        subscribeObservers();
        navigation();
        return binding.getRoot();
    }

    private void subscribeObservers() {
        viewModel.observedAddresses().removeObservers(getViewLifecycleOwner());
        viewModel.observedAddresses().observe(getViewLifecycleOwner(), addresses -> {
            if (addresses != null) {
                AddressesFragment.this.adapter.refresh(addresses);
            }
        });
    }

    private void initialization() {
        adapter = new AddressesRecyclerAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.recyclerView.setAdapter(adapter);
    }

    private void navigation() {

        binding.fragmentAddressesAdd.setOnClickListener(v ->
                hostable.onInflate(v, getString(R.string.tag_fragment_add_address)));

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

        viewModel.getUserAddresses();
    }
}