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
import com.example.student.ui.checkin.CheckInActivity;

public class CheckInFragment extends Fragment {
    private TextView tvStatus, tvLastCheckIn;
    private Button btnCheckIn, btnCheckOut;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkin, container, false);
        
        initViews(view);
        setupClickListeners();
        loadCheckInData();
        
        return view;
    }

    private void initViews(View view) {
        tvStatus = view.findViewById(R.id.tv_status);
        tvLastCheckIn = view.findViewById(R.id.tv_last_checkin);
        btnCheckIn = view.findViewById(R.id.btn_checkin);
        btnCheckOut = view.findViewById(R.id.btn_checkout);
    }

    private void setupClickListeners() {
        btnCheckIn.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), CheckInActivity.class));
        });

        btnCheckOut.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), CheckInActivity.class));
        });
    }

    private void loadCheckInData() {
        // Simulate loading check-in data
        tvStatus.setText("Not Checked In");
        tvLastCheckIn.setText("Last Check-in: Yesterday 7:25 AM");
    }
}

