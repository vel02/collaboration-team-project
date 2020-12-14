package cs.collaboration.yescredit.ui.apply;

import android.view.View;

import cs.collaboration.yescredit.ui.apply.model.ApplicationForm;

public interface Hostable {

    void onEnlist(ApplicationForm applicationForm);

    void onSave();

    void onInflate(View view, String screen);
}
