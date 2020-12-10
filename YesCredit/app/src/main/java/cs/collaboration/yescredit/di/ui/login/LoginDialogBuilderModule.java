package cs.collaboration.yescredit.di.ui.login;

import cs.collaboration.yescredit.ui.login.dialog.PasswordResetDialog;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class LoginDialogBuilderModule {

    @ContributesAndroidInjector
    abstract PasswordResetDialog contributePasswordResetDialog();

}
