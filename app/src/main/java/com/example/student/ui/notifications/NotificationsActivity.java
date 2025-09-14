package com.example.student.ui.notifications;

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
import com.example.student.models.Notification;
import com.example.student.services.AuthService;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {
    private RecyclerView rvNotifications;
    private TextView tvNoNotifications;
    private Toolbar toolbar;
    
    private NotificationsAdapter adapter;
    private List<Notification> notificationList;
    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        
        initViews();
        setupToolbar();
        setupRecyclerView();
        loadNotifications();
        
        authService = AuthService.getInstance(this);
    }

    private void initViews() {
        rvNotifications = findViewById(R.id.rv_notifications);
        tvNoNotifications = findViewById(R.id.tv_no_notifications);
        toolbar = findViewById(R.id.toolbar);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.notifications));
        }
    }

    private void setupRecyclerView() {
        notificationList = new ArrayList<>();
        adapter = new NotificationsAdapter(notificationList);
        rvNotifications.setLayoutManager(new LinearLayoutManager(this));
        rvNotifications.setAdapter(adapter);
    }

    private void loadNotifications() {
        // Simulate loading notifications
        // In real implementation, this would fetch from Firebase
        simulateNotifications();
    }

    private void simulateNotifications() {
        notificationList.clear();
        
        // Create mock notifications
        Notification notification1 = new Notification(
                "notif_001",
                authService.getCurrentStudent().getStudentId(),
                "Bus Arrival",
                "Your bus will arrive in 5 minutes",
                Notification.TYPE_BUS_ARRIVAL
        );
        notification1.setCreatedAt(new java.util.Date(System.currentTimeMillis() - 3600000)); // 1 hour ago
        notificationList.add(notification1);
        
        Notification notification2 = new Notification(
                "notif_002",
                authService.getCurrentStudent().getStudentId(),
                "Schedule Change",
                "Bus pickup time changed to 7:45 AM",
                Notification.TYPE_SCHEDULE_CHANGE
        );
        notification2.setCreatedAt(new java.util.Date(System.currentTimeMillis() - 7200000)); // 2 hours ago
        notificationList.add(notification2);
        
        adapter.notifyDataSetChanged();
        updateNoNotificationsVisibility();
    }

    private void updateNoNotificationsVisibility() {
        if (notificationList.isEmpty()) {
            tvNoNotifications.setVisibility(View.VISIBLE);
            rvNotifications.setVisibility(View.GONE);
        } else {
            tvNoNotifications.setVisibility(View.GONE);
            rvNotifications.setVisibility(View.VISIBLE);
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

