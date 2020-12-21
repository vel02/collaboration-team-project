package cs.collaboration.yescredit.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import static cs.collaboration.yescredit.util.Utility.*;

public class CodeCardFormatter implements TextWatcher {

    private Fragment context;
    private LinearLayout root;
    private Button add;

    public void setViews(Fragment context, LinearLayout root, Button add) {
        this.context = context;
        this.root = root;
        this.add = add;
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

        if (s.toString().length() == 4) {
            root.setVisibility(View.VISIBLE);
            add.setVisibility(View.VISIBLE);
            hideSoftKeyboard(context);
        } else add.setVisibility(View.GONE);

    }


}
