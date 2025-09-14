package com.example.student.ui.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.student.R;
import com.example.student.models.Trip;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TripHistoryAdapter extends RecyclerView.Adapter<TripHistoryAdapter.TripViewHolder> {
    private List<Trip> tripList;
    private SimpleDateFormat dateFormat, timeFormat;

    public TripHistoryAdapter(List<Trip> tripList) {
        this.tripList = tripList;
        this.dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        this.timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trip_history, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        Trip trip = tripList.get(position);
        holder.bind(trip);
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    static class TripViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDate, tvCheckInTime, tvCheckOutTime, tvDuration, tvStatus;

        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvCheckInTime = itemView.findViewById(R.id.tv_checkin_time);
            tvCheckOutTime = itemView.findViewById(R.id.tv_checkout_time);
            tvDuration = itemView.findViewById(R.id.tv_duration);
            tvStatus = itemView.findViewById(R.id.tv_status);
        }

        public void bind(Trip trip) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            
            if (trip.getCheckInTime() != null) {
                tvDate.setText(dateFormat.format(trip.getCheckInTime()));
                tvCheckInTime.setText(timeFormat.format(trip.getCheckInTime()));
            }
            
            if (trip.getCheckOutTime() != null) {
                tvCheckOutTime.setText(timeFormat.format(trip.getCheckOutTime()));
            } else {
                tvCheckOutTime.setText("N/A");
            }
            
            if (trip.isCompleted() && trip.getCheckInTime() != null && trip.getCheckOutTime() != null) {
                long duration = trip.getTripDuration();
                long minutes = duration / (1000 * 60);
                tvDuration.setText(minutes + " min");
                tvStatus.setText("Completed");
                tvStatus.setTextColor(itemView.getContext().getColor(R.color.success));
            } else {
                tvDuration.setText("N/A");
                tvStatus.setText("In Progress");
                tvStatus.setTextColor(itemView.getContext().getColor(R.color.warning));
            }
        }
    }
}

