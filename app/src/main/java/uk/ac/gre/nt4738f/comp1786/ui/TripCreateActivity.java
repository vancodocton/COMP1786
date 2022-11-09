package uk.ac.gre.nt4738f.comp1786.ui;

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

public class TripCreateActivity extends AppCompatActivity implements DatePickerFragment.IPickedDateObserver {
    TripDbHelper dbHelper;
    IEntityValidator<Trip> tripValidator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_create);
        dbHelper = new TripDbHelper(getApplicationContext());
        tripValidator = new TripValidator();

        setTripCreateSaveBtn();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setTripCreateSaveBtn() {
        Button btnSave = findViewById(R.id.btnCreateTripSave);
        btnSave.setOnClickListener(view -> {
            Trip trip = getTripFromInputs();

            EntityState state = tripValidator.Validate(trip);
            if (!state.isValid) {
                displayAlert(TripCreateActivity.this, "Invalid Input", state.getSingleErrorMessage());
            } else {
                try {
                    long id = dbHelper.insertTripOrThrow(trip);

                    Log.i(TripCreateActivity.this.toString(), String.format("Created trip with id '%d'.", id));

                    setResult(RESULT_OK);
                    finish();
                } catch (Exception e) {
                    Log.e(TripCreateActivity.this.toString(), "Created trip failed.", e);
                }
            }
        });
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