package cs.collaboration.yescredit.ui.apply.fragment.two;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import cs.collaboration.yescredit.ui.apply.SessionManager;
import cs.collaboration.yescredit.ui.apply.model.UserForm;

public class StepTwoViewModel extends ViewModel {

    private static final String TAG = "StepTwoViewModel";

    private final SessionManager sessionManager;
    private final MediatorLiveData<UserForm> userForm;
    private final MutableLiveData<UploadNotification> uploadNotification;

    @Inject

    public StepTwoViewModel(final SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.userForm = new MediatorLiveData<>();
        this.uploadNotification = new MutableLiveData<>();
        this.uploadNotification.setValue(UploadNotification.UPLOADING);
        getLatestUserForm();
    }

    public void updateUploadNotification(UploadNotification notification) {
        this.uploadNotification.setValue(notification);
    }

    private void getLatestUserForm() {

        final LiveData<UserForm> source = sessionManager.observeUserForm();
        userForm.addSource(source, userForm -> {
            assert userForm != null;
            StepTwoViewModel.this.userForm.setValue(userForm);
            StepTwoViewModel.this.userForm.removeSource(source);
        });
    }

    public LiveData<UserForm> observedUserForm() {
        return userForm;
    }

    public LiveData<UploadNotification> observedUploadNotification() {
        return uploadNotification;
    }

    public enum UploadNotification {UPLOADING, UPLOADED}

}
