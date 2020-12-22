package cs.collaboration.yescredit.util;

import android.content.res.Resources;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import cs.collaboration.yescredit.R;

import static cs.collaboration.yescredit.util.Keys.PROJECT_PACKAGE;

public class CreditCardFormatter implements TextWatcher {

    private final Resources resources;


    private EditText number;
    private Button next;
    private ImageView image;

    private final String separator;
    private final int divider;

    public CreditCardFormatter(Resources resources, String separator, int divider) {
        this.resources = resources;
        this.separator = separator;
        this.divider = divider;

    }

    public void setViews(EditText number, Button next, ImageView image) {
        this.number = number;
        this.next = next;
        this.image = image;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (number.getText().length() == 19)
            setViewVisible(next);
        else
            setViewGone(next);

        setCardImage();
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s == null) {
            return;
        }

        String oldString = s.toString();
        String newString = getNewString(oldString);
        if (!newString.equals(oldString)) {
            s.replace(0, oldString.length(), getNewString(oldString));
        }

    }

    private void setCardImage() {
        Uri uri;
        if (!number.getText().toString().isEmpty()) {
            String character = String.valueOf(number.getText().toString().charAt(0));
            switch (character) {
                case "5":
                    uri = Uri.parse("android.resource://" + PROJECT_PACKAGE + "/drawable/" + resources.getString(R.string.card_master));
                    break;
                case "4":
                    uri = Uri.parse("android.resource://" + PROJECT_PACKAGE + "/drawable/" + resources.getString(R.string.card_visa));
                    break;
                case "3":
                    uri = Uri.parse("android.resource://" + PROJECT_PACKAGE + "/drawable/" + resources.getString(R.string.card_express));
                    break;
                default:
                    uri = Uri.parse("android.resource://" + PROJECT_PACKAGE + "/drawable/" + resources.getString(R.string.card_placeholder));
            }
        } else {
            uri = Uri.parse("android.resource://" + PROJECT_PACKAGE + "/drawable/" + resources.getString(R.string.card_placeholder));
        }
        image.setImageURI(uri);
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


    private void setViewVisible(View view) {
        view.setVisibility(View.VISIBLE);
    }

    private void setViewGone(View view) {
        view.setVisibility(View.GONE);
    }
}
