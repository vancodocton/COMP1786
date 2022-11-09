package uk.ac.gre.nt4738f.comp1786.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import uk.ac.gre.nt4738f.comp1786.R;
import uk.ac.gre.nt4738f.comp1786.core.entities.Expense;
import uk.ac.gre.nt4738f.comp1786.core.entities.Trip;
import uk.ac.gre.nt4738f.comp1786.infrastructure.TripDbHelper;

public class TripDetailsActivity extends AppCompatActivity implements DeleteConfirmDialogFragment.IOnButtonClickListener {
    public static final String EXTRA_TRIP_ID = "uk.ac.gre.nt4738f.comp1786.EXTRA_TRIP_ID";
    private TripDbHelper dbHelper;

    private int tripId;
    private Trip trip;
    private RecyclerView recyclerView;
    private boolean isLoadExpenses;
    private boolean isLoadTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);
        dbHelper = new TripDbHelper(getApplicationContext());
        tripId = getIntent().getIntExtra(EXTRA_TRIP_ID, 0);
        isLoadExpenses = true;
        isLoadTrip = true;
        recyclerView = findViewById(R.id.recyclerViewExpense);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isLoadExpenses) {
            setExpenseRecyclerView();
            isLoadExpenses = false;
        }
        if (isLoadTrip) {
            setTripView();
            isLoadTrip = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.trip_details_top_app_bar, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.add:
                Intent intent = new Intent(TripDetailsActivity.this, ExpenseCreateActivity.class);
                intent.putExtra(EXTRA_TRIP_ID, tripId);
                expenseCreateActivityResultLauncher.launch(intent);
                return true;
            case R.id.delete:
                DialogFragment deleteConfirmFragment = new DeleteConfirmDialogFragment("this trip");
                deleteConfirmFragment.show(getSupportFragmentManager(), "DeleteConfirm");
                return true;
            case R.id.edit:
                Intent editIntent = new Intent(TripDetailsActivity.this, TripEditActivity.class);
                editIntent.putExtra(EXTRA_TRIP_ID, tripId);
                tripEditActivityResultLauncher.launch(editIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private final ActivityResultLauncher<Intent> expenseCreateActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int resultCode = result.getResultCode();
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT)
                            .show();
                    isLoadExpenses = true;
                }
            });


    private final ActivityResultLauncher<Intent> tripEditActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int resultCode = result.getResultCode();
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(getApplicationContext(), "Edited Success.", Toast.LENGTH_SHORT)
                            .show();
                    trip = dbHelper.getTripById(tripId);
                    // setTripView();
                    isLoadTrip = true;
                }
            });

    @Override
    public void onDeleteConfirmDialogPositiveClick(DialogFragment dialog) {
        int row = dbHelper.deleteTripById(tripId);

        if (row == 1) {
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Cannot deleted", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void setExpenseRecyclerView() {
        ArrayList<Expense> expenses = dbHelper.listExpensesOfTrip(tripId);
        ExpenseRecyclerViewAdapter recyclerAdapter = new ExpenseRecyclerViewAdapter(expenses);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
    }

    private void setTripView() {
        trip = dbHelper.getTripById(tripId);
        if (trip == null) {
            Toast.makeText(getApplicationContext(), "Not found", Toast.LENGTH_LONG)
                    .show();
        } else
            displayTripView(findViewById(R.id.layoutTripDetails));
    }

    private void displayTripView(View view) {
        final TextView nameTxt = view.findViewById(R.id.textViewTripName);
        final TextView destinationTxt = view.findViewById(R.id.textViewDestination);
        final TextView dateTxt = view.findViewById(R.id.textViewDate);
        final TextView isRiskAssessment = view.findViewById(R.id.textViewRiskAssessment);

        nameTxt.setText(trip.name);
        destinationTxt.setText(trip.destination);
        dateTxt.setText(trip.date.toString());
        isRiskAssessment.setText(getString(R.string.is_risk_assessment_text, trip.isRiskAssessment.toString()));
    }
}