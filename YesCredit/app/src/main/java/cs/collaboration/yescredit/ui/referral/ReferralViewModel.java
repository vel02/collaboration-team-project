package cs.collaboration.yescredit.ui.referral;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

import javax.inject.Inject;

import cs.collaboration.yescredit.model.Code;
import cs.collaboration.yescredit.util.Keys;

public class ReferralViewModel extends ViewModel {

    private static final String TAG = "ReferralViewModel";

    private MutableLiveData<String> code = new MutableLiveData<>();

    @Inject
    public ReferralViewModel() {
        Log.d(TAG, "ReferralViewModel: view model is working...");
    }


    public void generateCode() {
        code.setValue(generatePin());
    }

    private String generatePin() {
        String id = UUID.randomUUID().toString().replace("-", "");
        if (id.length() < 8) {
            return this.generatePin();
        }
        return id.substring(0, 8).toUpperCase();
    }

    public void createNewCode(String value) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String codeId = reference.child(Keys.DATABASE_NODE_CODES)
                .push().getKey();
        if (user != null && codeId != null) {

            Code code = new Code();
            code.setCode(value);
            code.setCodeId(codeId);
            code.setFromId(user.getUid());
            code.setCode_status("not-used");
            code.setReferred_status("not-referred");

            reference.child(Keys.DATABASE_NODE_CODES)
                    .child(codeId).setValue(code);

        }
    }


    public LiveData<String> observeCode() {
        return code;
    }


}
