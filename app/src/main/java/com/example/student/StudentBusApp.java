package com.example.student;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import androidx.core.app.NotificationCompat;

import com.google.firebase.FirebaseApp;
import com.jakewharton.threetenabp.AndroidThreeTen;

public class StudentBusApp extends Application {
    public static final String CHANNEL_ID = "bus_tracking_channel";
    public static final String NOTIFICATION_CHANNEL_ID = "bus_notifications";
    public static final String EMERGENCY_CHANNEL_ID = "emergency_notifications";

    @Override
    public void onCreate() {
        super.onCreate();
        
        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        
        // Initialize ThreeTenABP for date/time handling
        AndroidThreeTen.init(this);
        
        // Create notification channels
        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = getSystemService(NotificationManager.class);

            // Bus tracking channel
            NotificationChannel busChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Bus Tracking",
                    NotificationManager.IMPORTANCE_LOW
            );
            busChannel.setDescription("Notifications for bus location updates");
            notificationManager.createNotificationChannel(busChannel);

            // General notifications channel
            NotificationChannel notificationChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "Bus Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationChannel.setDescription("General bus notifications and updates");
            notificationManager.createNotificationChannel(notificationChannel);

            // Emergency notifications channel
            NotificationChannel emergencyChannel = new NotificationChannel(
                    EMERGENCY_CHANNEL_ID,
                    "Emergency Alerts",
                    NotificationManager.IMPORTANCE_HIGH
            );
            emergencyChannel.setDescription("Emergency notifications and alerts");
            emergencyChannel.enableVibration(true);
            emergencyChannel.setShowBadge(true);
            notificationManager.createNotificationChannel(emergencyChannel);
        }
    }
}

