package cs.collaboration.yescredit.ui.apply.fragment.validation;

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

        validations();

    }

    private void validations() {
        if (tag != null && !tag.isEmpty() && tag.equals("one")
                && views != null && views.size() > 0) {
            EditText lastName = views.get(0);
            EditText firstName = views.get(1);
            EditText middleName = views.get(2);
            EditText dateOfBirth = views.get(3);

            toMove.setEnabled(!lastName.getText().toString().isEmpty() &&
                    !firstName.getText().toString().isEmpty() &&
                    !middleName.getText().toString().isEmpty() &&
                    !dateOfBirth.getText().toString().isEmpty());

        } else if (tag != null && !tag.isEmpty() && tag.equals("three")
                && views != null && views.size() > 0) {

            EditText street = views.get(0);
            EditText barangay = views.get(1);
            EditText city = views.get(2);
            EditText province = views.get(3);
            EditText postal = views.get(4);

            toMove.setEnabled(!street.getText().toString().isEmpty() &&
                    !barangay.getText().toString().isEmpty() &&
                    !city.getText().toString().isEmpty() &&
                    !province.getText().toString().isEmpty() &&
                    !postal.getText().toString().isEmpty());
        } else if (tag != null && !tag.isEmpty() && tag.equals("five")
                && views != null && views.size() > 0) {
            EditText describe = views.get(0);

            toMove.setEnabled(!describe.getText().toString().isEmpty());
        } else if (tag != null && !tag.isEmpty() && tag.equals("six")
                && views != null && views.size() > 0) {
            EditText sourceOfIncome = views.get(0);
            EditText incomePerMonth = views.get(1);

            toMove.setEnabled(!sourceOfIncome.getText().toString().isEmpty() &&
                    !incomePerMonth.getText().toString().isEmpty());
        }
    }


}
