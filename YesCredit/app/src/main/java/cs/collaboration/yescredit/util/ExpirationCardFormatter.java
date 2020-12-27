package cs.collaboration.yescredit.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import static cs.collaboration.yescredit.util.Utility.hideSoftKeyboard;

public class ExpirationCardFormatter implements TextWatcher {

    private final String separator;
    private final int divider;

    private Fragment context;
    private LinearLayout layout;
    private EditText text;
    private Button button;

    public ExpirationCardFormatter(String separator, int divider) {
        this.separator = separator;
        this.divider = divider;
    }

    public void setView(Fragment context, LinearLayout layout, EditText text, Button button) {
        this.context = context;
        this.layout = layout;
        this.text = text;
        this.button = button;
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

        if (s.toString().length() < 5 && !text.getText().toString().isEmpty()) {
            if (layout != null) layout.setVisibility(View.GONE);
            button.setVisibility(View.GONE);
        } else if (!text.getText().toString().isEmpty()) {
            if (layout != null) layout.setVisibility(View.VISIBLE);
            button.setVisibility(View.VISIBLE);
            hideSoftKeyboard(context);
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
