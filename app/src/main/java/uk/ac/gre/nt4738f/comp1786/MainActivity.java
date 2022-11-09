package uk.ac.gre.nt4738f.comp1786;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import uk.ac.gre.nt4738f.comp1786.core.entities.Trip;
import uk.ac.gre.nt4738f.comp1786.infrastructure.TripDbHelper;
import uk.ac.gre.nt4738f.comp1786.ui.DeleteConfirmDialogFragment;
import uk.ac.gre.nt4738f.comp1786.ui.TripCreateActivity;
import uk.ac.gre.nt4738f.comp1786.ui.TripDetailsActivity;
import uk.ac.gre.nt4738f.comp1786.ui.TripRecyclerViewAdapter;
import uk.ac.gre.nt4738f.comp1786.ui.UploadActivity;

public class MainActivity extends AppCompatActivity
        implements DeleteConfirmDialogFragment.IOnButtonClickListener,
        TripRecyclerViewAdapter.IOnViewHolderListener {
    TripDbHelper dbHelper;
    private RecyclerView recyclerView;
    private boolean isReload = false;
    private TripRecyclerViewAdapter recyclerAdapter;
    private ArrayList<Trip> trips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new TripDbHelper(getApplicationContext());

        Toolbar topAppBar = (Toolbar) findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);
        topAppBar.setTitle(getTitle());

        recyclerView = findViewById(R.id.recyclerViewTrips);

        setTripRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.trip_list_top_app_bar, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent intent = new Intent(MainActivity.this, TripCreateActivity.class);
                tripCreateActivityResultLauncher.launch(intent);
                return true;
            case R.id.delete:
                DialogFragment deleteConfirmFragment = new DeleteConfirmDialogFragment("all trips");
                deleteConfirmFragment.show(getSupportFragmentManager(), "DeleteConfirm");
                return true;
            case R.id.upload:
                Intent uploadIntent = new Intent(MainActivity.this, UploadActivity.class);
                startActivity(uploadIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        if (isReload) {
            trips.clear();
            trips.addAll(dbHelper.listTrips());
            recyclerAdapter.notifyDataSetChanged();
            isReload = false;
        }
    }

    private final ActivityResultLauncher<Intent> tripCreateActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int resultCode = result.getResultCode();
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT)
                            .show();
                    isReload = true;
                }
            });

    private final ActivityResultLauncher<Intent> tripDetailsActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int resultCode = result.getResultCode();
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(getApplicationContext(), "Deleted Success.", Toast.LENGTH_SHORT)
                            .show();
                    isReload = true;
                }
            });

    private void setTripRecyclerView() {
        trips = dbHelper.listTrips();
        recyclerAdapter = new TripRecyclerViewAdapter(this, trips);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onDeleteConfirmDialogPositiveClick(DialogFragment dialog) {
        int row = dbHelper.deleteAllTrips();
        Toast.makeText(this, "Deleted all " + row + "trips.", Toast.LENGTH_SHORT)
                .show();
        isReload = true;
        trips.clear();
        recyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTripViewHolderClick(int tripId) {
        Intent intent = new Intent(this, TripDetailsActivity.class);
        intent.putExtra(TripDetailsActivity.EXTRA_TRIP_ID, tripId);

        tripDetailsActivityResultLauncher.launch(intent);
    }
}