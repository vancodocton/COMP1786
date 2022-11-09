package uk.ac.gre.nt4738f.comp1786.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import uk.ac.gre.nt4738f.comp1786.R;
import uk.ac.gre.nt4738f.comp1786.core.entities.Trip;


public class TripRecyclerViewAdapter extends RecyclerView.Adapter<TripRecyclerViewAdapter.TripViewHolder> {
    public interface IOnViewHolderListener {
        void onTripViewHolderClick(int tripId);
    }

    private final IOnViewHolderListener listener;
    private final List<Trip> trips;

    public TripRecyclerViewAdapter(IOnViewHolderListener listener, List<Trip> items) {
        this.listener = listener;
        trips = items;
    }

    @Override
    @NotNull
    public TripViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_recycler_trip_item, parent, false);
        return new TripViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TripViewHolder viewHolder, int position) {
        final Trip trip = trips.get(position);
        viewHolder.nameTxt.setText(trip.name);
        viewHolder.destinationTxt.setText(trip.destination);
        viewHolder.dateTxt.setText(trip.date.toString());
        if (trip.isRiskAssessment)
            viewHolder.isRiskAssessment.setText(viewHolder.view.getContext().getString(R.string.is_risk_assessment_text, "True"));
        else
            viewHolder.isRiskAssessment.setText(viewHolder.view.getContext().getString(R.string.is_risk_assessment_text, "False"));

        viewHolder.view.setOnClickListener(view -> listener.onTripViewHolderClick(trip.id));
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    public static class TripViewHolder extends RecyclerView.ViewHolder {
        public final TextView nameTxt;
        public final TextView destinationTxt;
        public final TextView dateTxt;
        public final TextView isRiskAssessment;
        public final View view;

        public TripViewHolder(final View view) {
            super(view);
            this.view = view;
            nameTxt = view.findViewById(R.id.textViewTripName);
            destinationTxt = view.findViewById(R.id.textViewDestination);
            dateTxt = view.findViewById(R.id.textViewDate);
            isRiskAssessment = view.findViewById(R.id.textViewRiskAssessment);
        }
    }
}