package com.example.student.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.student.R;
import com.example.student.models.Student;
import com.example.student.services.AuthService;
import com.example.student.ui.maps.MapsActivity;
import com.example.student.ui.schedule.ScheduleActivity;
import com.example.student.ui.checkin.CheckInActivity;
import com.google.android.material.card.MaterialCardView;

public class HomeFragment extends Fragment {
    private TextView tvWelcome, tvBusInfo, tvNextPickup;
    private MaterialCardView cardTracking, cardSchedule, cardCheckIn;
    
    private AuthService authService;
    private Student currentStudent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        initViews(view);
        setupClickListeners();
        loadStudentData();
        
        return view;
    }

    private void initViews(View view) {
        tvWelcome = view.findViewById(R.id.tv_welcome);
        tvBusInfo = view.findViewById(R.id.tv_bus_info);
        tvNextPickup = view.findViewById(R.id.tv_next_pickup);
        cardTracking = view.findViewById(R.id.card_tracking);
        cardSchedule = view.findViewById(R.id.card_schedule);
        cardCheckIn = view.findViewById(R.id.card_checkin);
        
        authService = AuthService.getInstance(requireContext());
    }

    private void setupClickListeners() {
        cardTracking.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), MapsActivity.class));
        });

        cardSchedule.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), ScheduleActivity.class));
        });

        cardCheckIn.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), CheckInActivity.class));
        });
    }

    private void loadStudentData() {
        currentStudent = authService.getCurrentStudent();
        if (currentStudent != null) {
            tvWelcome.setText("Welcome back, " + currentStudent.getName() + "!");
            tvBusInfo.setText("Bus: " + (currentStudent.getAssignedBusId() != null ? currentStudent.getAssignedBusId() : "Not assigned"));
            tvNextPickup.setText("Next pickup: 7:30 AM");
        }
    }
}

