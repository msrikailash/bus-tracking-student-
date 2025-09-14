package com.example.student.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.student.R;
import com.example.student.models.Student;
import com.example.student.services.AuthService;
import com.example.student.ui.fragments.HomeFragment;
import com.example.student.ui.fragments.TrackingFragment;
import com.example.student.ui.fragments.ScheduleFragment;
import com.example.student.ui.fragments.CheckInFragment;
import com.example.student.ui.fragments.NotificationsFragment;
import com.example.student.ui.fragments.HistoryFragment;
import com.example.student.ui.fragments.ProfileFragment;
import com.example.student.ui.profile.ProfileActivity;
import com.example.student.ui.history.TripHistoryActivity;
import com.example.student.ui.notifications.NotificationsActivity;
import com.example.student.ui.settings.SettingsActivity;
import com.example.student.ui.auth.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private TextView tvWelcome;
    
    private AuthService authService;
    private Student currentStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initViews();
        setupToolbar();
        setupNavigation(savedInstanceState);
        checkAuthStatus();
    }

    private void initViews() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        toolbar = findViewById(R.id.toolbar);
        
        View headerView = navigationView.getHeaderView(0);
        tvWelcome = headerView.findViewById(R.id.tv_welcome);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setupNavigation(Bundle savedInstanceState) {
        navigationView.setNavigationItemSelectedListener(this);
        bottomNavigationView.setOnItemSelectedListener(this::onBottomNavigationItemSelected);
        
        // Set default fragment
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }
    }

    private void checkAuthStatus() {
        authService = AuthService.getInstance(this);
        
        if (!authService.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        
        currentStudent = authService.getCurrentStudent();
        if (currentStudent != null) {
            tvWelcome.setText("Welcome, " + currentStudent.getName());
            
            // Start cross-app integration
            authService.startCrossAppIntegration();
        }
    }

    private boolean onBottomNavigationItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        
        if (itemId == R.id.nav_home) {
            loadFragment(new HomeFragment());
            return true;
        } else if (itemId == R.id.nav_tracking) {
            loadFragment(new TrackingFragment());
            return true;
        } else if (itemId == R.id.nav_schedule) {
            loadFragment(new ScheduleFragment());
            return true;
        } else if (itemId == R.id.nav_checkin) {
            loadFragment(new CheckInFragment());
            return true;
        } else if (itemId == R.id.nav_notifications) {
            loadFragment(new NotificationsFragment());
            return true;
        }
        
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        
        if (itemId == R.id.nav_home) {
            loadFragment(new HomeFragment());
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        } else if (itemId == R.id.nav_tracking) {
            loadFragment(new TrackingFragment());
            bottomNavigationView.setSelectedItemId(R.id.nav_tracking);
        } else if (itemId == R.id.nav_schedule) {
            loadFragment(new ScheduleFragment());
            bottomNavigationView.setSelectedItemId(R.id.nav_schedule);
        } else if (itemId == R.id.nav_checkin) {
            loadFragment(new CheckInFragment());
            bottomNavigationView.setSelectedItemId(R.id.nav_checkin);
        } else if (itemId == R.id.nav_notifications) {
            startActivity(new Intent(this, NotificationsActivity.class));
        } else if (itemId == R.id.nav_history) {
            startActivity(new Intent(this, TripHistoryActivity.class));
        } else if (itemId == R.id.nav_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
        } else if (itemId == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (itemId == R.id.nav_logout) {
            logout();
        }
        
        drawerLayout.closeDrawer(navigationView);
        return true;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    private void logout() {
        authService.logout();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawer(navigationView);
        } else {
            super.onBackPressed();
        }
    }
}
