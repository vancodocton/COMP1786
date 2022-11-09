package uk.ac.gre.nt4738f.comp1786.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

import uk.ac.gre.nt4738f.comp1786.R;
import uk.ac.gre.nt4738f.comp1786.core.entities.Trip;
import uk.ac.gre.nt4738f.comp1786.infrastructure.TripDbHelper;

public class TripSearchActivity extends AppCompatActivity {

    private TripDbHelper dbHelper;
    private ArrayList<Trip> trips;
    private TripRecyclerViewAdapter recyclerAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_search);
        dbHelper = new TripDbHelper(getApplicationContext());
        recyclerView = findViewById(R.id.searchRecyclerView);

        handleIntent(getIntent());
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            ArrayList<Trip> trips = dbHelper.searchTripByKeyword(query);
        }
    }
}