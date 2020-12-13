package cs.collaboration.yescredit.ui.apply.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

import cs.collaboration.yescredit.R;
import cs.collaboration.yescredit.ui.apply.ApplyActivity;
import cs.collaboration.yescredit.ui.apply.fragment.StepOneFragment;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        ApplyActivity activity = (ApplyActivity) getActivity();
        assert activity != null;

        StepOneFragment fragment = (StepOneFragment) activity.getSupportFragmentManager().findFragmentByTag(getString(R.string.tag_fragment_step_one));
        if (fragment != null) {
            fragment.processDatePickerResult(year, month, dayOfMonth);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }
}
