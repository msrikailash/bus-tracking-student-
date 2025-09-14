package com.example.student.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.student.R;
import com.example.student.ui.schedule.ScheduleActivity;

public class ScheduleFragment extends Fragment {
    private TextView tvNextPickup, tvNextDropoff;
    private Button btnViewSchedule;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        
        initViews(view);
        setupClickListeners();
        loadScheduleData();
        
        return view;
    }

    private void initViews(View view) {
        tvNextPickup = view.findViewById(R.id.tv_next_pickup);
        tvNextDropoff = view.findViewById(R.id.tv_next_dropoff);
        btnViewSchedule = view.findViewById(R.id.btn_view_schedule);
    }

    private void setupClickListeners() {
        btnViewSchedule.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), ScheduleActivity.class));
        });
    }

    private void loadScheduleData() {
        // Simulate loading schedule data
        tvNextPickup.setText("Next Pickup: 7:30 AM");
        tvNextDropoff.setText("Next Dropoff: 3:45 PM");
    }
}

