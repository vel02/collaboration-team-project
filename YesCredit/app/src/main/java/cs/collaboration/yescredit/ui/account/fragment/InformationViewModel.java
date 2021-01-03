package cs.collaboration.yescredit.ui.account.fragment;

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

import cs.collaboration.yescredit.model.User;

import static cs.collaboration.yescredit.util.Keys.DATABASE_FIELD_USER_ID_WITH_UNDERSCORE;
import static cs.collaboration.yescredit.util.Keys.DATABASE_NODE_USER;

public class InformationViewModel extends ViewModel {

    private static final String TAG = "InformationViewModel";

    private final DatabaseReference reference;
    private final FirebaseUser currentUser;

    private final MutableLiveData<User> user;
    private final MutableLiveData<String> email;

    @Inject
    public InformationViewModel() {
        this.reference = FirebaseDatabase.getInstance().getReference();
        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();
        this.user = new MutableLiveData<>();
        this.email = new MutableLiveData<>();
        getUserInformation();
    }

    private void getUserInformation() {
        if (currentUser != null) {

            Query query = reference.child(DATABASE_NODE_USER)
                    .orderByChild(DATABASE_FIELD_USER_ID_WITH_UNDERSCORE)
                    .equalTo(currentUser.getUid());

            getUserEmail();

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot singleShot : snapshot.getChildren()) {
                        User user = singleShot.getValue(User.class);
                        assert user != null;
                        InformationViewModel.this.user.postValue(user);
                        return;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    public LiveData<User> observedUserInformation() {
        return user;
    }

    public LiveData<String> observedUserEmail() {
        return email;
    }

    private void getUserEmail() {
        email.setValue(currentUser.getEmail());
    }


}
