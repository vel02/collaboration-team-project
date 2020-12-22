package cs.collaboration.yescredit.ui.account.fragment.information.card;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.databinding.FragmentAddBillingBinding;
import cs.collaboration.yescredit.model.Address;
import cs.collaboration.yescredit.ui.account.Hostable;
import cs.collaboration.yescredit.util.Keys;
import dagger.android.support.DaggerFragment;

import static cs.collaboration.yescredit.util.Utility.hideSoftKeyboard;


public class AddBillingFragment extends DaggerFragment {

    private static final String TAG = "AddBillingFragment";

    private FragmentAddBillingBinding binding;
    private Activity activity;
    private Hostable hostable;

    private boolean withInitialAddress;
    private Address address;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddBillingBinding.inflate(inflater);
        navigation();
        return binding.getRoot();
    }

    private void navigation() {

        binding.fragmentAddBillingSave.setOnClickListener(v -> {

            if (!binding.fragmentAddBillingStreet.getText().toString().isEmpty()
                    && !binding.fragmentAddBillingCity.getText().toString().isEmpty()) {
                //save
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                String street = binding.fragmentAddBillingStreet.getText().toString();
                String barangay = binding.fragmentAddBillingBarangay.getText().toString().isEmpty() ? "" : binding.fragmentAddBillingBarangay.getText().toString();
                String city = binding.fragmentAddBillingCity.getText().toString();
                String zip = binding.fragmentAddBillingZipcode.getText().toString().isEmpty() ? "" : binding.fragmentAddBillingZipcode.getText().toString();
                String province = binding.fragmentAddBillingProvince.getText().toString().isEmpty() ? "" : binding.fragmentAddBillingProvince.getText().toString();

                if (withInitialAddress && address != null) {
                    withInitialAddress = false;

                    address.setAddress_street(street);
                    address.setAddress_barangay(barangay);
                    address.setAddress_city(city);
                    address.setAddress_zipcode(zip);
                    address.setAddress_province(province);
                    address.setAddress_status("primary");
                    address.setAddress_selected("selected");

                    reference.child(getString(R.string.database_node_address))
                            .child(address.getAddress_id()).setValue(address);

                    reset();

                } else {

                    String addressId = reference.child(Keys.DATABASE_NODE_ADDRESS)
                            .push().getKey();

                    if (currentUser != null && addressId != null) {

                        Address address = new Address();
                        address.setUser_id(currentUser.getUid());
                        address.setAddress_id(addressId);
                        address.setAddress_street(street);
                        address.setAddress_barangay(barangay);
                        address.setAddress_city(city);
                        address.setAddress_zipcode(zip);
                        address.setAddress_province(province);
                        address.setAddress_status("not-primary");
                        address.setAddress_selected("not-selected");

                        reference.child(Keys.DATABASE_NODE_ADDRESS)
                                .child(addressId).setValue(address);

                        reset();
                    }
                }
            }

            if (binding.fragmentAddBillingStreet.getText().toString().isEmpty()) {
                binding.fragmentAddBillingStreet.requestFocus();
                binding.fragmentAddBillingStreet.setSelection(0);
            } else if (binding.fragmentAddBillingCity.getText().toString().isEmpty()) {
                binding.fragmentAddBillingCity.requestFocus();
                binding.fragmentAddBillingCity.setSelection(0);
            }

        });

    }

    private void reset() {
        hideSoftKeyboard(AddBillingFragment.this);
        requireActivity().onBackPressed();
    }

    private void checkInitialAddress() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {

            Query query = reference.child(getString(R.string.database_node_address))
                    .orderByChild(getString(R.string.database_field_user_id_underscore))
                    .equalTo(currentUser.getUid());

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot singleShot : snapshot.getChildren()) {

                        Address address = singleShot.getValue(Address.class);
                        assert address != null;
                        if (address.getAddress_status().equals("initial")) {
                            withInitialAddress = true;
                            AddBillingFragment.this.address = address;
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
        checkInitialAddress();
    }
}