package uk.ac.gre.nt4738f.comp1786;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import uk.ac.gre.nt4738f.comp1786.ui.IOnRecycleItemClickListener;
import uk.ac.gre.nt4738f.comp1786.ui.TripCreateActivity;
import uk.ac.gre.nt4738f.comp1786.ui.TripDetailsActivity;
import uk.ac.gre.nt4738f.comp1786.ui.TripRecyclerViewAdapter;
import uk.ac.gre.nt4738f.comp1786.ui.UploadActivity;

public class MainActivity extends AppCompatActivity implements DeleteConfirmDialogFragment.Listener {
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
            case R.id.search:
                Toast.makeText(
                        getApplicationContext(),
                        "You asked to exit, but why not start another app?",
                        Toast.LENGTH_LONG
                ).show();
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
        recyclerAdapter = new TripRecyclerViewAdapter(new TripOnClickListener(MainActivity.this), trips);
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

    public class TripOnClickListener implements IOnRecycleItemClickListener {
        private final Context context;

        public TripOnClickListener(Context context) {

            this.context = context;
        }

        public void onClick(int tripId) {
            Intent intent = new Intent(context, TripDetailsActivity.class);
            intent.putExtra(TripDetailsActivity.EXTRA_TRIP_ID, tripId);

            tripDetailsActivityResultLauncher.launch(intent);
        }
    }

}