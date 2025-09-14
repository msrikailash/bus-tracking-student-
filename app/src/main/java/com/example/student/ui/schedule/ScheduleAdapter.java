package com.example.student.ui.schedule;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.student.R;
import com.example.student.models.Schedule;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {
    private List<Schedule> scheduleList;
    private SimpleDateFormat timeFormat;

    public ScheduleAdapter(List<Schedule> scheduleList) {
        this.scheduleList = scheduleList;
        this.timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_schedule, parent, false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        Schedule schedule = scheduleList.get(position);
        holder.bind(schedule);
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public void updateSchedule(List<Schedule> newScheduleList) {
        this.scheduleList = newScheduleList;
        notifyDataSetChanged();
    }

    static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTime, tvType, tvStop, tvNotes;

        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvType = itemView.findViewById(R.id.tv_type);
            tvStop = itemView.findViewById(R.id.tv_stop);
            tvNotes = itemView.findViewById(R.id.tv_notes);
        }

        public void bind(Schedule schedule) {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            
            // Determine if it's pickup or dropoff based on time
            String type = "Pickup";
            String time = timeFormat.format(schedule.getPickupTime());
            
            // For demo purposes, we'll show both pickup and dropoff times
            if (schedule.getDropoffTime() != null) {
                time = timeFormat.format(schedule.getPickupTime()) + " - " + 
                       timeFormat.format(schedule.getDropoffTime());
                type = "Full Schedule";
            }
            
            tvTime.setText(time);
            tvType.setText(type);
            tvStop.setText("Bus Stop " + schedule.getStopId());
            
            if (schedule.getNotes() != null && !schedule.getNotes().isEmpty()) {
                tvNotes.setText(schedule.getNotes());
                tvNotes.setVisibility(View.VISIBLE);
            } else {
                tvNotes.setVisibility(View.GONE);
            }
        }
    }
}

