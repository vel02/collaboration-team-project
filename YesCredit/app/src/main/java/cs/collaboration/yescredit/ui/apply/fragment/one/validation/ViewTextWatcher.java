package cs.collaboration.yescredit.ui.apply.fragment.one.validation;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class ViewTextWatcher implements TextWatcher {

    private static final String TAG = "ViewTextWatcher";

    private final List<EditText> views;
    private final Button toMove;

    private final String tag;

    public ViewTextWatcher(List<EditText> views, Button toMove, String tag) {
        this.views = views;
        this.toMove = toMove;
        this.tag = tag;
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

        if (tag != null && !tag.isEmpty() && tag.equals("one")
                && views != null && views.size() > 0) {
            EditText lastName = views.get(0);
            EditText firstName = views.get(1);
            EditText middleName = views.get(2);
            EditText dateOfBirth = views.get(2);

            toMove.setEnabled(!lastName.getText().toString().isEmpty() &&
                    !firstName.getText().toString().isEmpty() &&
                    !middleName.getText().toString().isEmpty() &&
                    !dateOfBirth.getText().toString().isEmpty());

        }

    }
}
