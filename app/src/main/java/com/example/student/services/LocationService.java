package com.example.student.services;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.student.R;
import com.example.student.StudentBusApp;
import com.example.student.ui.MainActivity;
import com.example.student.utils.Constants;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationService extends Service {
    private static final String TAG = "LocationService";
    private static final int NOTIFICATION_ID = 1001;
    
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Location lastKnownLocation;
    private Context context;

    public LocationService() {
        // Default constructor
    }

    public LocationService(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (context == null) {
            context = this;
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        createLocationCallback();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Check if we have the required permissions before starting foreground service
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) 
                != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) 
                != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "Location permissions not granted, stopping service");
            stopSelf();
            return START_NOT_STICKY;
        }
        
        createNotificationChannel();
        startForeground(NOTIFICATION_ID, createNotification());
        startLocationUpdates();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    lastKnownLocation = location;
                    Log.d(TAG, "Location updated: " + location.getLatitude() + ", " + location.getLongitude());
                    // Here you would send location to Firebase or handle location updates
                }
            }
        };
    }

    public void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) 
                != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "Location permission not granted");
            return;
        }

        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(Constants.LOCATION_UPDATE_INTERVAL)
                .setFastestInterval(Constants.LOCATION_FASTEST_INTERVAL);

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    public void stopLocationUpdates() {
        if (locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    StudentBusApp.CHANNEL_ID,
                    "Bus Tracking",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("Location tracking for bus monitoring");
            
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private Notification createNotification() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, notificationIntent, 
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        return new NotificationCompat.Builder(this, StudentBusApp.CHANNEL_ID)
                .setContentTitle("Bus Tracking Active")
                .setContentText("Monitoring your location for bus tracking")
                .setSmallIcon(R.drawable.ic_bus)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .build();
    }

    public Location getLastKnownLocation() {
        return lastKnownLocation;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }
}
