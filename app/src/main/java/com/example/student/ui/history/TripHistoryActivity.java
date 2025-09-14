package com.example.student.ui.history;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.student.R;
import com.example.student.models.Trip;
import com.example.student.services.AuthService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TripHistoryActivity extends AppCompatActivity {
    private RecyclerView rvTripHistory;
    private TextView tvNoHistory;
    private Toolbar toolbar;
    
    private TripHistoryAdapter adapter;
    private List<Trip> tripList;
    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_history);
        
        initViews();
        setupToolbar();
        setupRecyclerView();
        loadTripHistory();
        
        authService = AuthService.getInstance(this);
    }

    private void initViews() {
        rvTripHistory = findViewById(R.id.rv_trip_history);
        tvNoHistory = findViewById(R.id.tv_no_history);
        toolbar = findViewById(R.id.toolbar);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.trip_history));
        }
    }

    private void setupRecyclerView() {
        tripList = new ArrayList<>();
        adapter = new TripHistoryAdapter(tripList);
        rvTripHistory.setLayoutManager(new LinearLayoutManager(this));
        rvTripHistory.setAdapter(adapter);
    }

    private void loadTripHistory() {
        // Simulate loading trip history
        // In real implementation, this would fetch from Firebase
        simulateTripHistory();
    }

    private void simulateTripHistory() {
        tripList.clear();
        
        // Create mock trip history
        Trip trip1 = new Trip(
                "trip_001",
                authService.getCurrentStudent().getStudentId(),
                "bus_001",
                "stop_001"
        );
        trip1.setCheckInTime(new Date(System.currentTimeMillis() - 86400000)); // 1 day ago
        trip1.setCheckOutTime(new Date(System.currentTimeMillis() - 86400000 + 1800000)); // 30 minutes later
        trip1.setCompleted(true);
        trip1.setCheckInLocation("37.7749,-122.4194");
        trip1.setCheckOutLocation("37.7849,-122.4094");
        tripList.add(trip1);
        
        Trip trip2 = new Trip(
                "trip_002",
                authService.getCurrentStudent().getStudentId(),
                "bus_001",
                "stop_001"
        );
        trip2.setCheckInTime(new Date(System.currentTimeMillis() - 172800000)); // 2 days ago
        trip2.setCheckOutTime(new Date(System.currentTimeMillis() - 172800000 + 1800000)); // 30 minutes later
        trip2.setCompleted(true);
        trip2.setCheckInLocation("37.7749,-122.4194");
        trip2.setCheckOutLocation("37.7849,-122.4094");
        tripList.add(trip2);
        
        adapter.notifyDataSetChanged();
        updateNoHistoryVisibility();
    }

    private void updateNoHistoryVisibility() {
        if (tripList.isEmpty()) {
            tvNoHistory.setVisibility(View.VISIBLE);
            rvTripHistory.setVisibility(View.GONE);
        } else {
            tvNoHistory.setVisibility(View.GONE);
            rvTripHistory.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

