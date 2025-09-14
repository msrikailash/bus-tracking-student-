package com.example.student.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.student.R;
import com.example.student.services.AuthService;

public class SettingsActivity extends AppCompatActivity {
    private Switch switchNotifications, switchLocation, switchBiometric;
    private TextView tvVersion, tvLogout;
    
    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        initViews();
        setupToolbar();
        setupClickListeners();
        loadSettings();
        
        authService = AuthService.getInstance(this);
    }

    private void initViews() {
        switchNotifications = findViewById(R.id.switch_notifications);
        switchLocation = findViewById(R.id.switch_location);
        switchBiometric = findViewById(R.id.switch_biometric);
        tvVersion = findViewById(R.id.tv_version);
        tvLogout = findViewById(R.id.tv_logout);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.settings));
        }
    }

    private void setupClickListeners() {
        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Save notification preference
            // In real implementation, this would save to SharedPreferences
        });

        switchLocation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Save location preference
            // In real implementation, this would save to SharedPreferences
        });

        switchBiometric.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Save biometric preference
            // In real implementation, this would save to SharedPreferences
        });

        tvLogout.setOnClickListener(v -> {
            authService.logout();
            finish();
        });
    }

    private void loadSettings() {
        // Load settings from SharedPreferences
        // In real implementation, this would load from SharedPreferences
        
        tvVersion.setText("Version 1.0.0");
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

