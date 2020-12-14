package cs.collaboration.yescredit.ui.apply;

import cs.collaboration.yescredit.ui.apply.model.ApplicationForm;

public interface Hostable {

    void onFillUp(ApplicationForm applicationForm);

    void onInflate(String screen);
}
