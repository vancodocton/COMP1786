package uk.ac.gre.nt4738f.comp1786.ui;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

import uk.ac.gre.nt4738f.comp1786.MainActivity;
import uk.ac.gre.nt4738f.comp1786.R;
import uk.ac.gre.nt4738f.comp1786.core.entities.Trip;
import uk.ac.gre.nt4738f.comp1786.core.entities.validators.EntityState;
import uk.ac.gre.nt4738f.comp1786.core.entities.validators.IEntityValidator;
import uk.ac.gre.nt4738f.comp1786.core.entities.validators.TripValidator;
import uk.ac.gre.nt4738f.comp1786.infrastructure.TripDbHelper;

public class TripCreateActivity extends AppCompatActivity {
    TripDbHelper dbHelper;

    IEntityValidator<Trip> tripValidator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_create);
        dbHelper = new TripDbHelper(getApplicationContext());
        tripValidator = new TripValidator();

        Button btnSave = findViewById(R.id.btnCreateTripSave);
        btnSave.setOnClickListener(view -> clickOnSaveBtn());
    }

    public void clickOnSaveBtn() {
        Trip trip = getTripFromInputs();

        EntityState state = tripValidator.Validate(trip);

        if (!state.isValid) {
            displayAlert(TripCreateActivity.this, "Invalid Input", state.getSingleErrorMessage());
        } else {
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues cv = trip.toContentValues();

            try {
                long id = dbHelper.insertTripOrThrow(trip);

                Log.i(TripCreateActivity.this.toString(), String.format("Created trip with id '%d'.", id));

                Intent intent = new Intent(TripCreateActivity.this, MainActivity.class);

                startActivity(intent);
            } catch (Exception e) {
                Log.i(TripCreateActivity.this.toString(), "Created trip failed.", e);
                Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    public Trip getTripFromInputs() {
        EditText name = findViewById(R.id.editTextTripName);
        EditText destination = findViewById(R.id.editTextTripDestination);

        RadioGroup radioGroupTripCreateRiskAssessment = findViewById(R.id.radioGroupTripCreateRiskAssessment);
        RadioButton selectedRadioBtn = radioGroupTripCreateRiskAssessment
                .findViewById(radioGroupTripCreateRiskAssessment.getCheckedRadioButtonId());

        Boolean isRiskAssessment;
        {
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

        return Trip.New(name.getText().toString(),
                destination.getText().toString(),
                LocalDate.now(), isRiskAssessment, null);
    }

    public static void displayAlert(@NotNull Context context, @NotNull String title, @NotNull String message) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton("Back", (dialogInterface, i) -> {
                }).show();
    }
}