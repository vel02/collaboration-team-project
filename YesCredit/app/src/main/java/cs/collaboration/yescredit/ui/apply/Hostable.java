package cs.collaboration.yescredit.ui.apply;

import cs.collaboration.yescredit.ui.apply.model.UserInfo;

public interface Hostable {

    void onListen(UserInfo userInfo);

    void onInflate(String screen);
}
