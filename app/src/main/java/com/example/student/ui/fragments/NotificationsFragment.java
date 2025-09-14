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

public class NotificationsFragment extends Fragment {
    private TextView tvNoNotifications;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        
        initViews(view);
        loadNotifications();
        
        return view;
    }

    private void initViews(View view) {
        tvNoNotifications = view.findViewById(R.id.tv_no_notifications);
    }

    private void loadNotifications() {
        // Simulate loading notifications
        tvNoNotifications.setText("No notifications");
    }
}

