package com.example.student.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.example.student.services.LocationService;

public class LocationServiceManager {
    private static final String TAG = "LocationServiceManager";

    /**
     * Start the location service if permissions are granted
     * @param context The context to start the service from
     * @return true if service was started, false if permissions are missing
     */
    public static boolean startLocationService(Context context) {
        if (hasLocationPermissions(context)) {
            Log.d(TAG, "Starting location service");
            Intent serviceIntent = new Intent(context, LocationService.class);
            context.startForegroundService(serviceIntent);
            return true;
        } else {
            Log.w(TAG, "Cannot start location service: permissions not granted");
            return false;
        }
    }

    /**
     * Stop the location service
     * @param context The context to stop the service from
     */
    public static void stopLocationService(Context context) {
        Log.d(TAG, "Stopping location service");
        Intent serviceIntent = new Intent(context, LocationService.class);
        context.stopService(serviceIntent);
    }

    /**
     * Check if the app has the required location permissions
     * @param context The context to check permissions for
     * @return true if permissions are granted, false otherwise
     */
    public static boolean hasLocationPermissions(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) 
                == PackageManager.PERMISSION_GRANTED &&
               ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) 
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Check if the app has the required foreground service permissions
     * @param context The context to check permissions for
     * @return true if permissions are granted, false otherwise
     */
    public static boolean hasForegroundServicePermissions(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.FOREGROUND_SERVICE_LOCATION) 
                == PackageManager.PERMISSION_GRANTED;
    }
}
