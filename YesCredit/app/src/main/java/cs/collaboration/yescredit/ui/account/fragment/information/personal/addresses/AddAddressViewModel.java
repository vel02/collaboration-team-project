package cs.collaboration.yescredit.ui.account.fragment.information.personal.addresses;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import javax.inject.Inject;

import cs.collaboration.yescredit.model.Address;
import cs.collaboration.yescredit.util.Keys;

import static cs.collaboration.yescredit.util.Keys.DATABASE_FIELD_USER_ID_WITH_UNDERSCORE;
import static cs.collaboration.yescredit.util.Keys.DATABASE_NODE_ADDRESS;

public class AddAddressViewModel extends ViewModel {

    private static final String TAG = "AddAddressViewModel";

    private final DatabaseReference reference;
    private final FirebaseUser currentUser;

    private final MutableLiveData<Address> address;

    @Inject
    public AddAddressViewModel() {
        this.reference = FirebaseDatabase.getInstance().getReference();
        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();
        this.address = new MutableLiveData<>();
    }

    public void checkInitialAddress() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {

            Query query = reference.child(DATABASE_NODE_ADDRESS)
                    .orderByChild(DATABASE_FIELD_USER_ID_WITH_UNDERSCORE)
                    .equalTo(currentUser.getUid());

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot singleShot : snapshot.getChildren()) {

                        Address address = singleShot.getValue(Address.class);
                        assert address != null;
                        if (address.getAddress_status().equals("initial")) {
                            AddAddressViewModel.this.address.postValue(address);
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

    public void savePrimaryAddress(Address address) {
        reference.child(DATABASE_NODE_ADDRESS)
                .child(address.getAddress_id()).setValue(address);
    }

    public boolean saveOtherAddress(String street, String barangay, String city, String zip, String province) {
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
            return true;
        }
        return false;
    }

    public LiveData<Address> observedAddress() {
        return address;
    }
}
