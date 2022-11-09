package uk.ac.gre.nt4738f.comp1786.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.time.LocalDate;


public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private final int viewPickedDateId;
    private IPickedDateObserver listener;
    public interface IPickedDateObserver{
        void updateViewPickedDate(int viewPickedDateId, LocalDate date);
    }
    public DatePickerFragment(int viewPickedDateId) {

        this.viewPickedDateId = viewPickedDateId;
    }

    @Override
    public void onAttach(@NonNull Context context) {

        try {
            listener = (IPickedDateObserver) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
            Log.e(this.getClass().toString(), "Caller must be implemented DatePickerFragment.IIUpdateDate", e);
        }
        super.onAttach(context);
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

        listener.updateViewPickedDate(viewPickedDateId, date);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}