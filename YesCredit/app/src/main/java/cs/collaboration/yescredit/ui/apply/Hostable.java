package cs.collaboration.yescredit.ui.apply;

import android.view.View;

import cs.collaboration.yescredit.ui.apply.model.ApplicationForm;

public interface Hostable {

    void onFillUp(ApplicationForm applicationForm);

    void onInflate(View view, String screen);
}
