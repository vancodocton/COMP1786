package uk.ac.gre.nt4738f.comp1786.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.time.LocalDate;


public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private final int viewDatePickerId;

    public DatePickerFragment(int viewDatePickerId) {

        this.viewDatePickerId = viewDatePickerId;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LocalDate localDate = LocalDate.now();
        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        int day = localDate.getDayOfMonth();
        return new DatePickerDialog(getActivity(), this, year, --month, day);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        LocalDate date = LocalDate.of(year, ++month, day);

        if (getActivity() instanceof IUpdateDate){
            ((IUpdateDate) getActivity()).updateDatePicker(viewDatePickerId, date);
        }
        else {
            Log.e(this.getClass().getName(), "Cannot set date picked.");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}