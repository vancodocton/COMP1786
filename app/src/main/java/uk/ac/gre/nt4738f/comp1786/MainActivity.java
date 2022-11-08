package uk.ac.gre.nt4738f.comp1786;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import uk.ac.gre.nt4738f.comp1786.core.entities.Trip;
import uk.ac.gre.nt4738f.comp1786.infrastructure.TripDbHelper;
import uk.ac.gre.nt4738f.comp1786.ui.IOnRecycleItemClickListener;
import uk.ac.gre.nt4738f.comp1786.ui.TripCreateActivity;
import uk.ac.gre.nt4738f.comp1786.ui.TripDetailsActivity;
import uk.ac.gre.nt4738f.comp1786.ui.TripRecyclerViewAdapter;

public class MainActivity extends AppCompatActivity {
    TripDbHelper dbHelper;
    private RecyclerView recyclerView;
    private Button createBtn;
    private boolean isReload = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new TripDbHelper(getApplicationContext());

        recyclerView = findViewById(R.id.recyclerViewTrips);
        createBtn = findViewById(R.id.btnCreateTrip);

        setTripCreateBtn();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isReload) {
            setTripRecyclerView();
            isReload = false;
        }
    }

    private void setTripCreateBtn() {
        createBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, TripCreateActivity.class);
            tripCreateActivityResultLauncher.launch(intent);
        });
    }

    private final ActivityResultLauncher<Intent> tripCreateActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int resultCode = result.getResultCode();
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT)
                            .show();
                    isReload = true;
                } else {
                    Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT)
                            .show();
                    isReload = false;
                }
            });

    private void setTripRecyclerView() {
        ArrayList<Trip> trips = dbHelper.listTrips();
        TripRecyclerViewAdapter recyclerAdapter = new TripRecyclerViewAdapter(new TripOnClickListener(MainActivity.this), trips);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
    }

    public class TripOnClickListener implements IOnRecycleItemClickListener {
        private final Context context;

        public TripOnClickListener(Context context) {

            this.context = context;
        }

        public void onClick(int tripId) {
            Intent intent = new Intent(context, TripDetailsActivity.class);
            intent.putExtra(TripDetailsActivity.EXTRA_TRIP_ID, tripId);
            startActivity(intent);
        }
    }
}