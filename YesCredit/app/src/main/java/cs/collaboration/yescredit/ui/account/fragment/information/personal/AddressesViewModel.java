package cs.collaboration.yescredit.ui.account.fragment.information.personal;

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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cs.collaboration.yescredit.model.Address;

import static cs.collaboration.yescredit.util.Keys.DATABASE_FIELD_USER_ID_WITH_UNDERSCORE;
import static cs.collaboration.yescredit.util.Keys.DATABASE_NODE_ADDRESS;

public class AddressesViewModel extends ViewModel {

    private static final String TAG = "AddressesViewModel";

    private final DatabaseReference reference;
    private final FirebaseUser currentUser;

    private final MutableLiveData<List<Address>> addresses;

    @Inject
    public AddressesViewModel() {
        this.reference = FirebaseDatabase.getInstance().getReference();
        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();
        this.addresses = new MutableLiveData<>();
    }

    public void getUserAddresses() {

        if (currentUser != null) {

            Query query = reference.child(DATABASE_NODE_ADDRESS)
                    .orderByChild(DATABASE_FIELD_USER_ID_WITH_UNDERSCORE)
                    .equalTo(currentUser.getUid());

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<Address> addresses = new ArrayList<>();

                    for (DataSnapshot singleShot : snapshot.getChildren()) {
                        Address address = singleShot.getValue(Address.class);
                        assert address != null;
                        addresses.add(address);
                    }
                    if (!addresses.get(0).getAddress_status().equals("initial")) {
                        AddressesViewModel.this.addresses.postValue(addresses);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }

    public LiveData<List<Address>> observedAddresses() {
        return addresses;
    }

}
