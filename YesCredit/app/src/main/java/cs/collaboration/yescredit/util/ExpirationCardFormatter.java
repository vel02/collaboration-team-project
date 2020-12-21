package cs.collaboration.yescredit.util;

import android.text.Editable;
import android.text.TextWatcher;

public class ExpirationCardFormatter implements TextWatcher {

    private final String separator;
    private final int divider;

    public ExpirationCardFormatter(String separator, int divider) {
        this.separator = separator;
        this.divider = divider;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s == null) {
            return;
        }

        //month validation part I
        if (s.toString().equals("00")) {
            s.replace(0, s.toString().length(), "0");
        }

        //month validation part II
        if (!s.toString().isEmpty() && s.toString().length() == 1) {

            byte number = Byte.parseByte(s.toString());

            if (number >= 2 && number <= 9) {
                s.replace(0, s.toString().length(), "0" + s.toString());
            }
        }

        //month validation part III
        if (s.toString().length() == 2) {
            byte number = Byte.parseByte(s.toString());

            if (number >= 13) {
                s.replace(0, s.toString().length(), s.toString().substring(0, s.toString().length() - 1));
            }
        }

        String oldString = s.toString();
        String newString = getNewString(oldString);
        if (!newString.equals(oldString)) {
            s.replace(0, oldString.length(), getNewString(oldString));
        }
    }

    private String getNewString(String value) {

        int divider = this.divider;

        String newString = value.replace(separator, "");

        while (newString.length() >= divider) {
            newString = newString.substring(0, divider - 1) + separator + newString.substring(divider - 1);
            divider += this.divider + separator.length() - 1;
        }
        return newString;
    }

}
