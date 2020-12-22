package cs.collaboration.yescredit.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import static cs.collaboration.yescredit.util.Utility.hideSoftKeyboard;

public class CodeCardFormatter implements TextWatcher {

    private Fragment context;
    private LinearLayout address;
    private Button add;
    private EditText expiration;

    public void setViews(Fragment context, LinearLayout address, Button add, EditText expiration) {
        this.context = context;
        this.address = address;
        this.add = add;
        this.expiration = expiration;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s == null) return;

        if (s.toString().length() < 4 && !expiration.getText().toString().isEmpty()) {
            address.setVisibility(View.GONE);
            add.setVisibility(View.GONE);
        } else if (!expiration.getText().toString().isEmpty()) {
            address.setVisibility(View.VISIBLE);
            add.setVisibility(View.VISIBLE);
            hideSoftKeyboard(context);
        }

//        if (s.toString().length() == 4) {
//            address.setVisibility(View.VISIBLE);
//            add.setVisibility(View.VISIBLE);
//            hideSoftKeyboard(context);
//        } else add.setVisibility(View.GONE);

    }


}