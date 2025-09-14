package com.example.student.ui.schedule;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.student.R;
import com.example.student.models.Schedule;
import com.example.student.services.AuthService;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ScheduleActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private RecyclerView rvSchedule;
    private TextView tvNoSchedule;
    private View progressBar;
    
    private AuthService authService;
    private ScheduleAdapter scheduleAdapter;
    private List<Schedule> scheduleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        
        initViews();
        setupTabs();
        setupRecyclerView();
        loadScheduleData();
        
        authService = AuthService.getInstance(this);
    }

    private void initViews() {
        tabLayout = findViewById(R.id.tab_layout);
        rvSchedule = findViewById(R.id.rv_schedule);
        tvNoSchedule = findViewById(R.id.tv_no_schedule);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("Today"));
        tabLayout.addTab(tabLayout.newTab().setText("Tomorrow"));
        tabLayout.addTab(tabLayout.newTab().setText("This Week"));
        
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        loadTodaySchedule();
                        break;
                    case 1:
                        loadTomorrowSchedule();
                        break;
                    case 2:
                        loadWeeklySchedule();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupRecyclerView() {
        scheduleList = new ArrayList<>();
        scheduleAdapter = new ScheduleAdapter(scheduleList);
        rvSchedule.setLayoutManager(new LinearLayoutManager(this));
        rvSchedule.setAdapter(scheduleAdapter);
    }

    private void loadScheduleData() {
        showProgress(true);
        
        // Simulate loading schedule data
        // In real implementation, this would fetch from Firebase
        simulateScheduleData();
    }

    private void simulateScheduleData() {
        scheduleList.clear();
        
        // Create mock schedule data
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        
        // Morning pickup
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 30);
        Date morningPickup = calendar.getTime();
        
        // Afternoon dropoff
        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE, 45);
        Date afternoonDropoff = calendar.getTime();
        
        Schedule morningSchedule = new Schedule(
                "schedule_001",
                "route_001",
                "stop_001",
                "monday",
                morningPickup,
                afternoonDropoff
        );
        morningSchedule.setNotes("Regular pickup time");
        scheduleList.add(morningSchedule);
        
        Schedule afternoonSchedule = new Schedule(
                "schedule_002",
                "route_001",
                "stop_001",
                "monday",
                afternoonDropoff,
                morningPickup
        );
        afternoonSchedule.setNotes("Regular dropoff time");
        scheduleList.add(afternoonSchedule);
        
        scheduleAdapter.notifyDataSetChanged();
        showProgress(false);
        updateNoScheduleVisibility();
    }

    private void loadTodaySchedule() {
        // Filter schedule for today
        List<Schedule> todaySchedule = new ArrayList<>();
        String today = getDayOfWeek(new Date());
        
        for (Schedule schedule : scheduleList) {
            if (schedule.getDayOfWeek().equals(today)) {
                todaySchedule.add(schedule);
            }
        }
        
        scheduleAdapter.updateSchedule(todaySchedule);
        updateNoScheduleVisibility();
    }

    private void loadTomorrowSchedule() {
        // Filter schedule for tomorrow
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        String tomorrow = getDayOfWeek(calendar.getTime());
        
        List<Schedule> tomorrowSchedule = new ArrayList<>();
        for (Schedule schedule : scheduleList) {
            if (schedule.getDayOfWeek().equals(tomorrow)) {
                tomorrowSchedule.add(schedule);
            }
        }
        
        scheduleAdapter.updateSchedule(tomorrowSchedule);
        updateNoScheduleVisibility();
    }

    private void loadWeeklySchedule() {
        // Show all schedule
        scheduleAdapter.updateSchedule(scheduleList);
        updateNoScheduleVisibility();
    }

    private String getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                return Schedule.MONDAY;
            case Calendar.TUESDAY:
                return Schedule.TUESDAY;
            case Calendar.WEDNESDAY:
                return Schedule.WEDNESDAY;
            case Calendar.THURSDAY:
                return Schedule.THURSDAY;
            case Calendar.FRIDAY:
                return Schedule.FRIDAY;
            case Calendar.SATURDAY:
                return Schedule.SATURDAY;
            case Calendar.SUNDAY:
                return Schedule.SUNDAY;
            default:
                return Schedule.MONDAY;
        }
    }

    private void updateNoScheduleVisibility() {
        if (scheduleAdapter.getItemCount() == 0) {
            tvNoSchedule.setVisibility(View.VISIBLE);
            rvSchedule.setVisibility(View.GONE);
        } else {
            tvNoSchedule.setVisibility(View.GONE);
            rvSchedule.setVisibility(View.VISIBLE);
        }
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}

