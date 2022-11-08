package uk.ac.gre.nt4738f.comp1786.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import uk.ac.gre.nt4738f.comp1786.R;
import uk.ac.gre.nt4738f.comp1786.core.entities.Expense;
import uk.ac.gre.nt4738f.comp1786.core.entities.Trip;
import uk.ac.gre.nt4738f.comp1786.infrastructure.TripDbHelper;

public class TripDetailsActivity extends AppCompatActivity {
    public static final String EXTRA_TRIP_ID = "uk.ac.gre.nt4738f.comp1786.EXTRA_TRIP_ID";
    private TripDbHelper dbHelper;

    private int tripId;
    private Trip trip;
    private RecyclerView recyclerView;
    private Button createBtn;
    private boolean isLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);
        dbHelper = new TripDbHelper(getApplicationContext());
        tripId = getIntent().getIntExtra(EXTRA_TRIP_ID, 0);
        isLoad = true;

        recyclerView = findViewById(R.id.recyclerViewExpense);
        createBtn = findViewById(R.id.btnCreateExpense);

        setTripView();
        setExpenseCreateBtn();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isLoad) {
            setExpenseRecyclerView();
            isLoad = false;
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

    public void setExpenseCreateBtn() {
        createBtn.setOnClickListener(view -> {
            Intent intent = new Intent(TripDetailsActivity.this, ExpenseCreateActivity.class);
            intent.putExtra(EXTRA_TRIP_ID, tripId);
            expenseCreateActivityResultLauncher.launch(intent);
        });
    }

    private final ActivityResultLauncher<Intent> expenseCreateActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int resultCode = result.getResultCode();
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT)
                            .show();
                    isLoad = true;
                } else {
                    Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT)
                            .show();
                    isLoad = false;
                }
            });
}