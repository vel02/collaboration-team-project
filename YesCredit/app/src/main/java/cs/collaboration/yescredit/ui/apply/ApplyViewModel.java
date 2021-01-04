package cs.collaboration.yescredit.ui.apply;

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

import cs.collaboration.yescredit.model.Card;
import cs.collaboration.yescredit.ui.apply.model.LoanForm;
import cs.collaboration.yescredit.ui.apply.model.UserForm;
import cs.collaboration.yescredit.util.Keys;

import static cs.collaboration.yescredit.ui.apply.ApplyViewModel.State.AVAILABLE;
import static cs.collaboration.yescredit.ui.apply.ApplyViewModel.State.NOT_AVAILABLE;
import static cs.collaboration.yescredit.util.Keys.DATABASE_FIELD_BARANGAY_ADDRESS;
import static cs.collaboration.yescredit.util.Keys.DATABASE_FIELD_CITY_ADDRESS;
import static cs.collaboration.yescredit.util.Keys.DATABASE_FIELD_DATE_OF_BIRTH;
import static cs.collaboration.yescredit.util.Keys.DATABASE_FIELD_FIRST_NAME;
import static cs.collaboration.yescredit.util.Keys.DATABASE_FIELD_GENDER;
import static cs.collaboration.yescredit.util.Keys.DATABASE_FIELD_GOVERNMENT_IMAGE;
import static cs.collaboration.yescredit.util.Keys.DATABASE_FIELD_LAST_NAME;
import static cs.collaboration.yescredit.util.Keys.DATABASE_FIELD_MIDDLE_NAME;
import static cs.collaboration.yescredit.util.Keys.DATABASE_FIELD_POSTAL_ADDRESS;
import static cs.collaboration.yescredit.util.Keys.DATABASE_FIELD_PROVINCE_ADDRESS;
import static cs.collaboration.yescredit.util.Keys.DATABASE_FIELD_STREET_ADDRESS;
import static cs.collaboration.yescredit.util.Keys.DATABASE_NODE_USER;

public class ApplyViewModel extends ViewModel {

    private static final String TAG = "ApplyViewModel";

    @Inject
    SessionManager sessionManager;

    private final DatabaseReference reference;
    private final FirebaseUser user;

    private final MutableLiveData<State> stateCard;


    @Inject
    public ApplyViewModel() {
        this.reference = FirebaseDatabase.getInstance().getReference();
        this.user = FirebaseAuth.getInstance().getCurrentUser();
        this.stateCard = new MutableLiveData<>();

    }

    public void saveUserInputForm(UserForm form) {
        if (form != null) {
            Log.d(TAG, "onChanged: form: " + form);
            assert user != null;
            save(reference, user.getUid(), DATABASE_FIELD_LAST_NAME, form.getLast_name() != null ? form.getLast_name() : "");
            save(reference, user.getUid(), DATABASE_FIELD_FIRST_NAME, form.getFirst_name() != null ? form.getFirst_name() : "");
            save(reference, user.getUid(), DATABASE_FIELD_MIDDLE_NAME, form.getMiddle_name() != null ? form.getMiddle_name() : "");
            save(reference, user.getUid(), DATABASE_FIELD_GENDER, form.getGender() != null ? form.getGender() : "");
            save(reference, user.getUid(), DATABASE_FIELD_DATE_OF_BIRTH, form.getDate_of_birth() != null ? form.getDate_of_birth() : "");
            save(reference, user.getUid(), DATABASE_FIELD_GOVERNMENT_IMAGE, form.getGovernment_id() != null ? form.getGovernment_id() : "");
            save(reference, user.getUid(), DATABASE_FIELD_STREET_ADDRESS, form.getStreet_address() != null ? form.getStreet_address() : "");
            save(reference, user.getUid(), DATABASE_FIELD_BARANGAY_ADDRESS, form.getBarangay_address() != null ? form.getBarangay_address() : "");
            save(reference, user.getUid(), DATABASE_FIELD_CITY_ADDRESS, form.getCity_address() != null ? form.getCity_address() : "");
            save(reference, user.getUid(), DATABASE_FIELD_PROVINCE_ADDRESS, form.getProvince_address() != null ? form.getProvince_address() : "");
            save(reference, user.getUid(), DATABASE_FIELD_POSTAL_ADDRESS, form.getPostal_address() != null ? form.getPostal_address() : "");
        }
    }

    public void getUserPrimaryCard() {
        if (user != null) {

            Query query = reference.child(Keys.DATABASE_NODE_CARDS)
                    .orderByChild(Keys.DATABASE_FIELD_USER_ID_WITH_UNDERSCORE)
                    .equalTo(user.getUid());

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean isAvailable = false;
                    for (DataSnapshot singleShot : snapshot.getChildren()) {

                        Card current = singleShot.getValue(Card.class);
                        assert current != null;
                        if (current.getCard_status().equals("primary")) {
                            isAvailable = true;
                            ApplyViewModel.this.stateCard.postValue(AVAILABLE);
                        }
                    }

                    if (!isAvailable) {
                        ApplyViewModel.this.stateCard.postValue(NOT_AVAILABLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }


    public void setUserForm(UserForm userForm) {
        sessionManager.setUserForm(userForm);
    }

    public void setLoanForm(LoanForm loanForm) {
        sessionManager.setLoanForm(loanForm);
    }

    public void resetForm() {
        sessionManager.setLoanForm(new LoanForm());
        sessionManager.setUserForm(new UserForm());
    }

    private void save(DatabaseReference reference, String userId, String field, String value) {
        reference.child(DATABASE_NODE_USER)
                .child(userId)
                .child(field).setValue(value);
    }

    public LiveData<State> observedStateCard() {
        return stateCard;
    }

    public enum State {AVAILABLE, NOT_AVAILABLE}

}
