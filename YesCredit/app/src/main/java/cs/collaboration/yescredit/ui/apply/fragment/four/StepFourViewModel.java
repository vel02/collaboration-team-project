package cs.collaboration.yescredit.ui.apply.fragment.four;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
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
import cs.collaboration.yescredit.ui.apply.SessionManager;
import cs.collaboration.yescredit.ui.apply.model.UserForm;

import static cs.collaboration.yescredit.util.Keys.DATABASE_FIELD_ADDRESS_SELECTED;
import static cs.collaboration.yescredit.util.Keys.DATABASE_FIELD_ADDRESS_STATUS;
import static cs.collaboration.yescredit.util.Keys.DATABASE_FIELD_USER_ID_WITH_UNDERSCORE;
import static cs.collaboration.yescredit.util.Keys.DATABASE_NODE_ADDRESS;

public class StepFourViewModel extends ViewModel {

    private static final String TAG = "StepFourViewModel";
    private final SessionManager sessionManager;
    private final DatabaseReference reference;
    private final FirebaseUser user;
    private final MediatorLiveData<UserForm> userForm;
    private final MutableLiveData<Address> initialAddress;

    @Inject
    public StepFourViewModel(final SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.reference = FirebaseDatabase.getInstance().getReference();
        this.user = FirebaseAuth.getInstance().getCurrentUser();
        this.userForm = new MediatorLiveData<>();
        this.initialAddress = new MutableLiveData<>();
        getLatestUserForm();
        getInitialAddress();
    }

    private void getInitialAddress() {

        if (user != null) {

            Query query = reference.child(DATABASE_NODE_ADDRESS)
                    .orderByChild(DATABASE_FIELD_USER_ID_WITH_UNDERSCORE)
                    .equalTo(user.getUid());

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot singleShot : snapshot.getChildren()) {

                        Address current = singleShot.getValue(Address.class);
                        if (current.getAddress_status().equals("initial")) {
                            initialAddress.postValue(current);
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

    public void updateInitialAddress(Address address) {

        if (user != null) {

            reference.child(DATABASE_NODE_ADDRESS)
                    .child(address.getAddress_id())
                    .setValue(address);


            reference.child(DATABASE_NODE_ADDRESS)
                    .child(address.getAddress_id())
                    .child(DATABASE_FIELD_ADDRESS_STATUS)
                    .setValue("primary");

            reference.child(DATABASE_NODE_ADDRESS)
                    .child(address.getAddress_id())
                    .child(DATABASE_FIELD_ADDRESS_SELECTED)
                    .setValue("selected");

        }

    }

    private void getLatestUserForm() {

        final LiveData<UserForm> source = sessionManager.observeUserForm();
        userForm.addSource(source, userForm -> {
            assert userForm != null;
            StepFourViewModel.this.userForm.setValue(userForm);
            StepFourViewModel.this.userForm.removeSource(source);
        });
    }

    public LiveData<UserForm> observedUserForm() {
        return userForm;
    }

    public LiveData<Address> observedInitialAddress() {
        return initialAddress;
    }

}

