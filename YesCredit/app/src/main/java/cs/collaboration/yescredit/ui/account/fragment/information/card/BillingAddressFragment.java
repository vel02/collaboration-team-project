package cs.collaboration.yescredit.ui.account.fragment.information.card;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.FragmentBillingAddressBinding;
import cs.collaboration.yescredit.model.Address;
import cs.collaboration.yescredit.ui.account.Hostable;
import cs.collaboration.yescredit.ui.account.adapter.address.AddressRecyclerAdapter;
import dagger.android.support.DaggerFragment;

public class BillingAddressFragment extends DaggerFragment implements AddressRecyclerAdapter.OnAddressRecyclerListener {

    @Override
    public void onClickAddress(Address address) {
        Log.d(TAG, "onClickAddress: address " + address);
        changeSelectedAddress(address);

    }

    private static final String TAG = "BillingAddressFragment";

    private FragmentBillingAddressBinding binding;
    private DatabaseReference reference;
    private Activity activity;
    private Hostable hostable;

    private AddressRecyclerAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBillingAddressBinding.inflate(inflater);
        initialization();
        navigation();
        return binding.getRoot();
    }

    private void changeSelectedAddress(Address address) {

        FirebaseUser current = FirebaseAuth.getInstance().getCurrentUser();

        if (current != null) {

            Query query = reference.child(getString(R.string.database_node_address))
                    .orderByChild(getString(R.string.database_field_user_id_underscore))
                    .equalTo(current.getUid());

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot singleShot : snapshot.getChildren()) {

                        Address current = singleShot.getValue(Address.class);
                        assert current != null;
                        if (current.getAddress_selected().equals("selected")) {
                            current.setAddress_selected("not-selected");

                            reference.child(getString(R.string.database_node_address))
                                    .child(current.getAddress_id())
                                    .setValue(current);

                            assert address != null;
                            address.setAddress_selected("selected");
                            reference.child(getString(R.string.database_node_address))
                                    .child(address.getAddress_id())
                                    .setValue(address);

                            getUserAddresses();
                            return;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


    }

    private void getUserAddresses() {

        FirebaseUser current = FirebaseAuth.getInstance().getCurrentUser();

        if (current != null) {

            Query query = reference.child(getString(R.string.database_node_address))
                    .orderByChild(getString(R.string.database_field_user_id_underscore))
                    .equalTo(current.getUid());

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<Address> addresses = new ArrayList<>();
                    for (DataSnapshot singleShot : snapshot.getChildren()) {

                        Address address = singleShot.getValue(Address.class);
                        assert address != null;
                        addresses.add(address);

                    }

                    BillingAddressFragment.this.adapter.refresh(addresses);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }

    private void initialization() {
        reference = FirebaseDatabase.getInstance().getReference();

        adapter = new AddressRecyclerAdapter();
        adapter.setOnAddressRecyclerListener(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.recyclerView.setAdapter(adapter);
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

        getUserAddresses();
    }

}