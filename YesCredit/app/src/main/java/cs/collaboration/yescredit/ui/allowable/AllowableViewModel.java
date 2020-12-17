package cs.collaboration.yescredit.ui.allowable;

import android.util.Log;

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

import cs.collaboration.yescredit.model.Loan;
import cs.collaboration.yescredit.util.Keys;

public class AllowableViewModel extends ViewModel {

    private static final String TAG = "AllowableViewModel";

    private final MutableLiveData<Boolean> isNotAllowed = new MutableLiveData<>();

    @Inject
    public AllowableViewModel() {
        Log.d(TAG, "AllowableViewModel: view model is working...");
    }

    public void checkPendingLoan() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {

            Query query = reference.child(Keys.DATABASE_NODE_LOAN)
                    .orderByChild(Keys.DATABASE_FIELD_USER_ID)
                    .equalTo(user.getUid());

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    long number_of_transaction = 0;
                    for (DataSnapshot singleShot : snapshot.getChildren()) {
                        number_of_transaction++;
                        Loan loan = singleShot.getValue(Loan.class);
                        Log.d(TAG, "onDataChange: done!");
                        if (loan.getStatus().toLowerCase().equals("on-going")) {
                            isNotAllowed.postValue(true);
                            return;
                        }

                        if (number_of_transaction == snapshot.getChildrenCount()
                                && !loan.getStatus().toLowerCase().equals("on-going")) {
                            isNotAllowed.postValue(false);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    public LiveData<Boolean> observeIsAllowed() {
        return isNotAllowed;
    }

}