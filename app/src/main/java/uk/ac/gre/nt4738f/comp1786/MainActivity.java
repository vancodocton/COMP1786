package uk.ac.gre.nt4738f.comp1786;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import uk.ac.gre.nt4738f.comp1786.core.entities.Trip;
import uk.ac.gre.nt4738f.comp1786.infrastructure.TripDbHelper;
import uk.ac.gre.nt4738f.comp1786.ui.TripCreateActivity;
import uk.ac.gre.nt4738f.comp1786.ui.TripRecyclerViewAdapter;

public class MainActivity extends AppCompatActivity {
    TripDbHelper dbHelper;
    private RecyclerView recyclerView;
    private ArrayList<Trip> trips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new TripDbHelper(getApplicationContext());

        Button createBtn = findViewById(R.id.btnCreateTrip);
        createBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, TripCreateActivity.class);
            startActivity(intent);
        });

        recyclerView = findViewById(R.id.recyclerViewTrips);

        trips = dbHelper.listTrips();
        setAdapter();
    }

    private void setAdapter() {
        TripRecyclerViewAdapter recyclerAdapter = new TripRecyclerViewAdapter(trips);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
    }
}