package uk.ac.gre.nt4738f.comp1786.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import uk.ac.gre.nt4738f.comp1786.R;
import uk.ac.gre.nt4738f.comp1786.core.entities.Expense;
import uk.ac.gre.nt4738f.comp1786.core.entities.Trip;
import uk.ac.gre.nt4738f.comp1786.infrastructure.TripDbHelper;

public class TripDetailsActivity extends AppCompatActivity {
    private TripDbHelper dbHelper;

    private ArrayList<Expense> expenses;
    private int tripId = 1;
    private Trip trip;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        dbHelper = new TripDbHelper(getApplicationContext());

        trip = dbHelper.getTripById(tripId);
        if (trip == null) {
            Toast.makeText(getApplicationContext(), "Not found", Toast.LENGTH_LONG)
                    .show();
        } else
            displayTrip(findViewById(R.id.layoutTripDetails));

        expenses = dbHelper.listExpensesOfTrip(tripId);
        recyclerView = findViewById(R.id.recyclerViewExpense);
        setExpenseRecyclerViewAdapter();

        Button createBtn = findViewById(R.id.btnCreateExpense);
        createBtn.setOnClickListener(view -> {
            Intent intent = new Intent(TripDetailsActivity.this, ExpenseCreateActivity.class);
            startActivity(intent);
        });
    }

    private void setExpenseRecyclerViewAdapter() {
        ExpenseRecyclerViewAdapter recyclerAdapter = new ExpenseRecyclerViewAdapter(expenses);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
    }

    private void displayTrip(View view) {
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