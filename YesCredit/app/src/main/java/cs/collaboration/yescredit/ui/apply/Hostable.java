package cs.collaboration.yescredit.ui.apply;

import android.view.View;

import cs.collaboration.yescredit.ui.apply.model.LoanForm;
import cs.collaboration.yescredit.ui.apply.model.UserForm;

public interface Hostable {

    void onEnlist(UserForm userForm);

    void onEnlist(LoanForm LoanForm);

    void onInflate(View view, String screen);
}
