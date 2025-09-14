package com.example.student.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.student.services.CrossAppIntegrationService;

public class CrossAppNotificationReceiver extends BroadcastReceiver {
    private static final String TAG = "CrossAppNotificationReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "Received broadcast: " + action);

        if (action != null) {
            switch (action) {
                case "MARK_AS_READ":
                    handleMarkAsRead(context, intent);
                    break;
                case "CALL_EMERGENCY":
                    handleCallEmergency(context, intent);
                    break;
                case "VIEW_LOCATION":
                    handleViewLocation(context, intent);
                    break;
                default:
                    Log.w(TAG, "Unknown action: " + action);
            }
        }
    }

    private void handleMarkAsRead(Context context, Intent intent) {
        String messageId = intent.getStringExtra("message_id");
        if (messageId != null) {
            Log.d(TAG, "Marking message as read: " + messageId);
            CrossAppIntegrationService.getInstance(context)
                    .markMessageAsRead(messageId)
                    .thenAccept(success -> {
                        if (success) {
                            Log.d(TAG, "Message marked as read successfully");
                        } else {
                            Log.e(TAG, "Failed to mark message as read");
                        }
                    });
        }
    }

    private void handleCallEmergency(Context context, Intent intent) {
        String emergencyNumber = intent.getStringExtra("emergency_number");
        if (emergencyNumber != null) {
            Log.d(TAG, "Calling emergency number: " + emergencyNumber);
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(android.net.Uri.parse("tel:" + emergencyNumber));
            callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(callIntent);
        }
    }

    private void handleViewLocation(Context context, Intent intent) {
        String busId = intent.getStringExtra("bus_id");
        Log.d(TAG, "Opening location view for bus: " + busId);
        
        Intent locationIntent = new Intent(context, com.example.student.ui.maps.MapsActivity.class);
        locationIntent.putExtra("bus_id", busId);
        locationIntent.putExtra("emergency", true);
        locationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(locationIntent);
    }
}
