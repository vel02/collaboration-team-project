package cs.collaboration.yescredit.ui.apply.fragment.one;

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

import cs.collaboration.yescredit.model.User;
import cs.collaboration.yescredit.ui.apply.SessionManager;
import cs.collaboration.yescredit.ui.apply.model.UserForm;
import cs.collaboration.yescredit.util.Keys;

public class StepOneViewModel extends ViewModel {

    private final MediatorLiveData<UserForm> userForm;
    private final SessionManager sessionManager;

    @Inject
    public StepOneViewModel(final SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.userForm = new MediatorLiveData<>();
        getLatestUserForm();
    }

    private void getLatestUserForm() {

        final LiveData<UserForm> source = sessionManager.observeUserForm();
        userForm.addSource(source, userForm -> {
            assert userForm != null;
            StepOneViewModel.this.userForm.setValue(userForm);
            StepOneViewModel.this.userForm.removeSource(source);
        });
    }

    public LiveData<UserForm> observedUserForm() {
        return userForm;
    }

}
