package uk.ac.gre.nt4738f.comp1786.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import uk.ac.gre.nt4738f.comp1786.R;
import uk.ac.gre.nt4738f.comp1786.core.entities.Trip;
import uk.ac.gre.nt4738f.comp1786.infrastructure.TripDbHelper;

public class TripSearchActivity extends AppCompatActivity
        implements TripRecyclerViewAdapter.IOnViewHolderListener {

    private TripDbHelper dbHelper;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_search);
        dbHelper = new TripDbHelper(getApplicationContext());
        recyclerView = findViewById(R.id.searchRecyclerView);

        handleIntent(getIntent());
    }

    private void setTripRecyclerView(ArrayList<Trip> trips) {
        TripRecyclerViewAdapter recyclerAdapter = new TripRecyclerViewAdapter(this, trips);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
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
            setTripRecyclerView(trips);
        }
    }

    @Override
    public void onTripViewHolderClick(int tripId) {
        Intent intent = new Intent(this, TripDetailsActivity.class);
        intent.putExtra(TripDetailsActivity.EXTRA_TRIP_ID, tripId);

        tripDetailsActivityResultLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> tripDetailsActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int resultCode = result.getResultCode();
                if (resultCode == RESULT_OK) {
                    Toast.makeText(getApplicationContext(), "Deleted Success.", Toast.LENGTH_SHORT)
                            .show();
                }
            });
}