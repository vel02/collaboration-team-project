package cs.collaboration.yescredit.ui.apply.fragment.four.address;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import cs.collaboration.yescredit.ui.apply.SessionManager;
import cs.collaboration.yescredit.ui.apply.model.UserForm;

public class AddressViewModel extends ViewModel {

    private static final String TAG = "AddressViewModel";
    private final SessionManager sessionManager;
    private final MediatorLiveData<UserForm> userForm;

    @Inject
    public AddressViewModel(final SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.userForm = new MediatorLiveData<>();
        getLatestUserForm();
    }

    private void getLatestUserForm() {

        final LiveData<UserForm> source = sessionManager.observeUserForm();
        userForm.addSource(source, userForm -> {
            assert userForm != null;
            AddressViewModel.this.userForm.setValue(userForm);
            AddressViewModel.this.userForm.removeSource(source);
        });
    }

    public LiveData<UserForm> observedUserForm() {
        return userForm;
    }

}
