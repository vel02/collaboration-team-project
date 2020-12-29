package cs.collaboration.yescredit.ui.apply;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;

import cs.collaboration.yescredit.ui.apply.model.LoanForm;
import cs.collaboration.yescredit.ui.apply.model.UserForm;

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

    @Inject
    public ApplyViewModel() {
        this.reference = FirebaseDatabase.getInstance().getReference();
        this.user = FirebaseAuth.getInstance().getCurrentUser();
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

}
