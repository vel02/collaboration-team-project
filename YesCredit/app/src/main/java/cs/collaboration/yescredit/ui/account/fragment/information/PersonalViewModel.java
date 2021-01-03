package cs.collaboration.yescredit.ui.account.fragment.information;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import javax.inject.Inject;

import cs.collaboration.yescredit.model.Address;
import cs.collaboration.yescredit.model.User;
import cs.collaboration.yescredit.util.Keys;

import static cs.collaboration.yescredit.util.Keys.DATABASE_FIELD_USER_ID_WITH_UNDERSCORE;
import static cs.collaboration.yescredit.util.Keys.DATABASE_NODE_USER;

public class PersonalViewModel extends ViewModel {

    private static final String TAG = "PersonalViewModel";

    private final DatabaseReference reference;
    private final FirebaseUser currentUser;

    private final MutableLiveData<Address> address;
    private final MutableLiveData<User> user;
    private final MutableLiveData<String> email;
    private final MutableLiveData<String> image;
    private final MutableLiveData<String> uploadNotification;


    @Inject
    public PersonalViewModel() {
        this.reference = FirebaseDatabase.getInstance().getReference();
        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();
        this.address = new MutableLiveData<>();
        this.user = new MutableLiveData<>();
        this.email = new MutableLiveData<>();
        this.image = new MutableLiveData<>();
        this.uploadNotification = new MutableLiveData<>();
    }

    public void uploadUserImage(byte[] bytes) {
        String FIREBASE_IMAGE_STORAGE = "images/users";
        final double MB_THRESHOLD = 5.0;
        final double MB = 1000000.0;

        final StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child(FIREBASE_IMAGE_STORAGE + "/" + FirebaseAuth.getInstance().getCurrentUser().getUid()
                        + "/profile_image");

        if (bytes.length / MB < MB_THRESHOLD) {

            UploadTask uploadTask = storageReference.putBytes(bytes);

            uploadTask.addOnSuccessListener(taskSnapshot -> {
                Task<Uri> firebaseURL = taskSnapshot.getMetadata().getReference().getDownloadUrl();

                firebaseURL.addOnSuccessListener(uri -> {
                    if (uri != null) {
                        Log.d(TAG, "onSuccess: firebase download url : " + uri.toString());

                        if (currentUser != null) {

                            reference.child(DATABASE_NODE_USER)
                                    .child(currentUser.getUid())
                                    .child(Keys.DATABASE_FIELD_PROFILE_IMAGE).setValue(uri.toString());

                            PersonalViewModel.this.image.postValue(uri.toString());
                        }

                    }
                }).addOnFailureListener(e -> setUploadNotification("could not upload photo"));
            }).addOnFailureListener(e -> setUploadNotification("could not upload photo"));
        } else {
            setUploadNotification("Image is too Large");
        }
    }

    private void setUploadNotification(String message) {
        PersonalViewModel.this.uploadNotification.postValue(message);
    }

    public void getUserInformation() {

        getUserPrimaryAddress();
        getUserEmail();

        Query query = reference.child(DATABASE_NODE_USER)
                .orderByChild(DATABASE_FIELD_USER_ID_WITH_UNDERSCORE)
                .equalTo(currentUser.getUid());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot singleShot : snapshot.getChildren()) {

                    User user = singleShot.getValue(User.class);
                    if (user != null) {
                        PersonalViewModel.this.user.postValue(user);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getUserPrimaryAddress() {

        Query query = reference.child(Keys.DATABASE_NODE_ADDRESS)
                .orderByChild(Keys.DATABASE_FIELD_USER_ID_WITH_UNDERSCORE)
                .equalTo(currentUser.getUid());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot singleShot : snapshot.getChildren()) {

                    Address address = singleShot.getValue(Address.class);
                    assert address != null;
                    if (address.getAddress_status().equals("primary")) {
                        PersonalViewModel.this.address.postValue(address);
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public LiveData<Address> observedAddress() {
        return address;
    }

    public LiveData<User> observedUser() {
        return user;
    }

    public LiveData<String> observedUserEmail() {
        return email;
    }

    public LiveData<String> observedUserImage() {
        return image;
    }

    public LiveData<String> observedUploadNotification() {
        return uploadNotification;
    }

    private void getUserEmail() {
        email.setValue(currentUser.getEmail());
    }


}
