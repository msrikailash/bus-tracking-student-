package com.example.student.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.student.R;

public class HistoryFragment extends Fragment {
    private TextView tvNoHistory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        
        initViews(view);
        loadHistory();
        
        return view;
    }

    private void initViews(View view) {
        tvNoHistory = view.findViewById(R.id.tv_no_history);
    }

    private void loadHistory() {
        // Simulate loading history
        tvNoHistory.setText("No trip history available");
    }
}

