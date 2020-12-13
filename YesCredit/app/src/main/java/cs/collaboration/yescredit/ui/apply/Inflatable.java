package cs.collaboration.yescredit.ui.apply;

import cs.collaboration.yescredit.ui.apply.model.ApplicationForm;

public interface Inflatable {

    void onListen(ApplicationForm applicationForm);

    void onInflate(String screen);
}
