package cs.collaboration.yescredit.ui.signup;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

import javax.inject.Inject;

import cs.collaboration.yescredit.model.Code;
import cs.collaboration.yescredit.model.Loan;
import cs.collaboration.yescredit.model.User;
import cs.collaboration.yescredit.util.Keys;

public class SignUpViewModel extends ViewModel {

    private static final String TAG = "SignUpViewModel";

    private final MutableLiveData<State> progressBarState = new MutableLiveData<>();
    private final MutableLiveData<Boolean> redirectToLoginScreen = new MutableLiveData<>();
    private final MutableLiveData<Notification> notifications = new MutableLiveData<>();
    private final FirebaseAuth auth;
    private final FirebaseDatabase database;


    @Inject
    public SignUpViewModel() {
        Log.d(TAG, "SignUpViewModel: view model is working...");
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        redirectToLoginScreen.setValue(false);
    }

    /*
        Resource Message
        - Unable to Register
        - Send Verification Email
        - Couldn't Send Verification Email
     */

    public void registerNewEmail(String email, String password, String code) {
        progressBarState.setValue(State.VISIBLE);

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "onComplete: " + task.isSuccessful());
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: uid: " + auth.getCurrentUser().getUid());

                            sendVerificationEmail();
                            createInitialLoan();
                            checkReferralCode(code);
                            createNewUserStorage();

                        } else {
                            Log.d(TAG, "onComplete: Unable to Register");
                        }
                        progressBarState.setValue(State.INVISIBLE);
                    }
                });
    }


    private void createInitialLoan() {
        DatabaseReference reference = database.getReference();

        FirebaseUser current = auth.getCurrentUser();

        String loanId = reference.child(Keys.DATABASE_NODE_LOAN)
                .push().getKey();

        if (current != null && loanId != null) {
            Loan loan = new Loan();
            loan.setUserId(current.getUid());
            loan.setLoanId(loanId);
            loan.setLevelOfEducation("");
            loan.setReason("");
            loan.setMoreDetails("");
            loan.setOutstanding("");
            loan.setCivilStatus("");
            loan.setSourceOfIncome("");
            loan.setIncomePerMonth("");
            loan.setLimit("10000");
            loan.setStatus("paid");
            loan.setRepayment_loan("");
            loan.setRepayment_date("");
            loan.setRepayment_interest("");
            loan.setRepayment_tax("");
            loan.setRepayment_penalty("");
            loan.setRepayment_total("");
            loan.setRepayment_days("");

            reference.child(Keys.DATABASE_NODE_LOAN)
                    .child(loanId).setValue(loan);

        }
    }

    public void createNewUserStorage() {
        FirebaseUser current = auth.getCurrentUser();
        if (current == null) return;
        User user = new User();
        user.setUser_id(current.getUid());
        user.setLast_name("");
        user.setFirst_name("");
        user.setMiddle_name("");
        user.setGender("");
        user.setDate_of_birth("");
        user.setStreet_address("");
        user.setBarangay_address("");
        user.setCity_address("");
        user.setProfile_image("");
        user.setProvince_address("");
        user.setPostal_address("");
        user.setGovernment_image("");
        user.setProfile_image("");

        database.getReference()
                .child(Keys.DATABASE_NODE_USER)
                .child(current.getUid())
                .setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        auth.signOut();
                        redirectToLoginScreen.setValue(true);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                auth.signOut();
                redirectToLoginScreen.setValue(true);
                Log.d(TAG, "onFailure: something went wrong");
            }
        });
    }

    private void checkReferralCode(String code) {
        if (code == null || code.isEmpty()) {
            notifications.postValue(Notification.REFERRAL_FAILED);
            return;
        }

        DatabaseReference reference = database.getReference();

        Query query = reference.child(Keys.DATABASE_NODE_CODES)
                .orderByChild(Keys.DATABASE_FIELD_CODE)
                .equalTo(code.toUpperCase());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String benefit = generateBenefit();
                Log.d(TAG, "onDataChange: benefit " + benefit);
                for (DataSnapshot singleShot : snapshot.getChildren()) {

                    Code generated = singleShot.getValue(Code.class);
                    if (generated != null) {
                        reference.child(Keys.DATABASE_NODE_CODES)
                                .child(generated.getCodeId())
                                .child(Keys.DATABASE_FIELD_REFERRED_STATUS)
                                .setValue("referred");

                        reference.child(Keys.DATABASE_NODE_CODES)
                                .child(generated.getCodeId())
                                .child(Keys.DATABASE_FIELD_CODE_BENEFIT)
                                .setValue(benefit);

                        notifications.postValue(Notification.REFERRAL_SUCCESS);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: " + error.getDetails());
            }
        });

    }

    private String generateBenefit() {
        final byte max = 4;
        final byte min = 1;
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return String.valueOf(randomNum);
    }

    public void sendVerificationEmail() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: Send Verification Email");
                    } else {
                        Log.d(TAG, "onComplete: Couldn't Send Verification Email");
                    }
                }
            });
        }
    }

    public LiveData<State> observeProgressBarState() {
        return progressBarState;
    }

    public LiveData<Boolean> observeDirectToLoginScreen() {
        return redirectToLoginScreen;
    }

    public LiveData<Notification> observeNotification() {
        return notifications;
    }

    public enum State {VISIBLE, INVISIBLE}

    public enum Notification {REFERRAL_SUCCESS, REFERRAL_FAILED}

}
