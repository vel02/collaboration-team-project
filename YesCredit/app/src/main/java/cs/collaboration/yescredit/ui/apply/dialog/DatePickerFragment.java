package cs.collaboration.yescredit.ui.apply.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = "DatePickerFragment";


    private OnDatePickerListener listener;

    public interface OnDatePickerListener {
        void onProcessDatePickerResult(int year, int month, int day);
    }

    public void setOnDatePickerListener(OnDatePickerListener listener) {
        this.listener = listener;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        listener.onProcessDatePickerResult(year, month, dayOfMonth);
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
