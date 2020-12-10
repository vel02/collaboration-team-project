package cs.collaboration.yescredit.util;

import android.app.Activity;
import android.view.WindowManager;

public class Utility {

    public static void hideSoftKeyboard(Activity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

}
