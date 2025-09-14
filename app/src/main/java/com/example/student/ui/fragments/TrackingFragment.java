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
import com.example.student.ui.maps.MapsActivity;

public class TrackingFragment extends Fragment {
    private TextView tvBusStatus, tvEta, tvNextStop;
    private Button btnViewMap, btnRefresh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tracking, container, false);
        
        initViews(view);
        setupClickListeners();
        loadTrackingData();
        
        return view;
    }

    private void initViews(View view) {
        tvBusStatus = view.findViewById(R.id.tv_bus_status);
        tvEta = view.findViewById(R.id.tv_eta);
        tvNextStop = view.findViewById(R.id.tv_next_stop);
        btnViewMap = view.findViewById(R.id.btn_view_map);
        btnRefresh = view.findViewById(R.id.btn_refresh);
    }

    private void setupClickListeners() {
        btnViewMap.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), MapsActivity.class));
        });

        btnRefresh.setOnClickListener(v -> {
            loadTrackingData();
        });
    }

    private void loadTrackingData() {
        // Simulate loading tracking data
        tvBusStatus.setText("Bus 12 is on route");
        tvEta.setText("ETA: 5 minutes");
        tvNextStop.setText("Next Stop: Main Street");
    }
}

