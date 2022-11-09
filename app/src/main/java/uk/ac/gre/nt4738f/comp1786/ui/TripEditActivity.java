package uk.ac.gre.nt4738f.comp1786.ui;

import static uk.ac.gre.nt4738f.comp1786.ui.TripDetailsActivity.EXTRA_TRIP_ID;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

import uk.ac.gre.nt4738f.comp1786.R;
import uk.ac.gre.nt4738f.comp1786.core.entities.Trip;
import uk.ac.gre.nt4738f.comp1786.core.entities.validators.EntityState;
import uk.ac.gre.nt4738f.comp1786.core.entities.validators.IEntityValidator;
import uk.ac.gre.nt4738f.comp1786.core.entities.validators.TripValidator;
import uk.ac.gre.nt4738f.comp1786.infrastructure.TripDbHelper;

public class TripEditActivity extends AppCompatActivity implements DatePickerFragment.IPickedDateObserver {
    TripDbHelper dbHelper;
    IEntityValidator<Trip> tripValidator;
    int tripId;
    private Trip trip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_create);
        tripId = getIntent().getIntExtra(EXTRA_TRIP_ID, 0);

        dbHelper = new TripDbHelper(getApplicationContext());
        trip = dbHelper.getTripById(tripId);
        tripValidator = new TripValidator();

        setTripEditSaveBtn();
        setTripForEdit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setTripEditSaveBtn() {
        Button btnEdit = findViewById(R.id.btnCreateTripSave);
        btnEdit.setOnClickListener(view -> {
            Trip trip = getTripFromInputs();

            EntityState state = tripValidator.Validate(trip);
            if (!state.isValid) {
                displayAlert(TripEditActivity.this, "Invalid Input", state.getSingleErrorMessage());
            } else {
                try {
                    long row = dbHelper.updateTripOrThrow(tripId, trip);

                    if (row == 1)
                        Log.i(TripEditActivity.this.toString(), String.format("Updated trip with id '%d'.", tripId));
                    setResult(RESULT_OK);
                    finish();
                } catch (Exception e) {
                    Log.e(TripEditActivity.this.toString(), "Created trip failed.", e);
                }
            }
        });
    }

    private void setTripForEdit() {
        ((EditText) findViewById(R.id.editTextTripName)).setText(trip.name);
        ((EditText) findViewById(R.id.editTextTripDestination))
                .setText(trip.destination);
        ((TextView) findViewById(R.id.textViewDatePicker)).setText(trip.date.toString());
        RadioGroup radioGroupTripCreateRiskAssessment = findViewById(R.id.radioGroupTripCreateRiskAssessment);

        if (trip.isRiskAssessment)
            ((RadioButton) findViewById(R.id.radioButtonYes)).setChecked(true);
        else
            ((RadioButton) findViewById(R.id.radioButtonNo)).setChecked(true);
    }

    public Trip getTripFromInputs() {
        String name = ((EditText) findViewById(R.id.editTextTripName))
                .getText()
                .toString();
        String destination = ((EditText) findViewById(R.id.editTextTripDestination))
                .getText()
                .toString();
        LocalDate date = null;
        try {
            String dateAsString = ((TextView) findViewById(R.id.textViewDatePicker))
                    .getText()
                    .toString();
            date = LocalDate.parse(dateAsString);
        } catch (Exception e) {
            Log.e(this.getClass().getName(), e.getMessage(), e);
        }

        Boolean isRiskAssessment;
        {
            RadioGroup radioGroupTripCreateRiskAssessment = findViewById(R.id.radioGroupTripCreateRiskAssessment);
            RadioButton selectedRadioBtn = radioGroupTripCreateRiskAssessment
                    .findViewById(radioGroupTripCreateRiskAssessment.getCheckedRadioButtonId());

            final String yes = getString(R.string.text_yes);
            final String no = getString(R.string.text_no);
            if (selectedRadioBtn == null)
                isRiskAssessment = null;
            else {
                final String value = selectedRadioBtn.getText().toString();
                if (value.equals(yes))
                    isRiskAssessment = true;
                else if (value.equals(no))
                    isRiskAssessment = false;
                else
                    isRiskAssessment = null;
            }
        }

        return Trip.New(name,
                destination,
                date, isRiskAssessment, null);
    }

    public static void displayAlert(@NotNull Context context, @NotNull String title, @NotNull String message) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton("Back", (dialogInterface, i) -> {
                }).show();
    }

    public void showTripDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment(R.id.textViewDatePicker);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void updateViewPickedDate(int viewPickedDateId, LocalDate date) {
        TextView viewTextDatedPicker = findViewById(viewPickedDateId);
        viewTextDatedPicker.setText(date.toString());
    }
}