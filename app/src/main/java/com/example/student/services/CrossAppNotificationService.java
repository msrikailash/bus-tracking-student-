package com.example.student.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.student.R;
import com.example.student.models.CrossAppMessage;
import com.example.student.receivers.CrossAppNotificationReceiver;
import com.example.student.ui.MainActivity;
import com.example.student.ui.maps.MapsActivity;
import com.example.student.utils.Constants;

public class CrossAppNotificationService {
    private static final String TAG = "CrossAppNotification";
    private static final String CHANNEL_ID_CROSS_APP = "cross_app_notifications";
    private static final String CHANNEL_ID_EMERGENCY = "emergency_notifications";
    private static final String CHANNEL_ID_LOCATION = "location_notifications";
    
    private Context context;
    private NotificationManager notificationManager;

    public CrossAppNotificationService(Context context) {
        this.context = context.getApplicationContext();
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannels();
    }

    /**
     * Create notification channels for different types of cross-app notifications
     */
    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Cross-app general notifications
            NotificationChannel crossAppChannel = new NotificationChannel(
                    CHANNEL_ID_CROSS_APP,
                    "Cross-App Messages",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            crossAppChannel.setDescription("Notifications from driver and supervisor apps");
            crossAppChannel.enableLights(true);
            crossAppChannel.enableVibration(true);
            notificationManager.createNotificationChannel(crossAppChannel);

            // Emergency notifications
            NotificationChannel emergencyChannel = new NotificationChannel(
                    CHANNEL_ID_EMERGENCY,
                    "Emergency Alerts",
                    NotificationManager.IMPORTANCE_HIGH
            );
            emergencyChannel.setDescription("Emergency notifications from drivers and supervisors");
            emergencyChannel.enableLights(true);
            emergencyChannel.enableVibration(true);
            emergencyChannel.setShowBadge(true);
            notificationManager.createNotificationChannel(emergencyChannel);

            // Location notifications
            NotificationChannel locationChannel = new NotificationChannel(
                    CHANNEL_ID_LOCATION,
                    "Location Updates",
                    NotificationManager.IMPORTANCE_LOW
            );
            locationChannel.setDescription("Bus location and route updates");
            locationChannel.enableLights(false);
            locationChannel.enableVibration(false);
            notificationManager.createNotificationChannel(locationChannel);
        }
    }

    /**
     * Show notification for cross-app message
     */
    public void showCrossAppNotification(CrossAppMessage message) {
        Log.d(TAG, "Showing cross-app notification: " + message.getTitle());

        String channelId = getChannelIdForMessage(message);
        int priority = getNotificationPriority(message.getPriority());
        
        // Create intent to open the app
        Intent intent = createIntentForMessage(message);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 
                message.getMessageId().hashCode(), 
                intent, 
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Create notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_bus_logo)
                .setContentTitle(message.getTitle())
                .setContentText(message.getContent())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(priority)
                .setCategory(getNotificationCategory(message.getMessageType()));

        // Add sound and vibration for high priority messages
        if (message.getPriority().equals(CrossAppIntegrationService.PRIORITY_HIGH) || 
            message.getPriority().equals(CrossAppIntegrationService.PRIORITY_URGENT)) {
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(soundUri);
            builder.setVibrate(new long[]{0, 300, 200, 300});
        }

        // Add actions for specific message types
        addNotificationActions(builder, message);

        // Show notification
        notificationManager.notify(message.getMessageId().hashCode(), builder.build());
    }

    /**
     * Show emergency notification with special handling
     */
    public void showEmergencyNotification(CrossAppMessage emergencyMessage) {
        Log.d(TAG, "Showing emergency notification: " + emergencyMessage.getTitle());

        // Create intent to open maps activity for emergency
        Intent intent = new Intent(context, MapsActivity.class);
        intent.putExtra("emergency", true);
        intent.putExtra("message_id", emergencyMessage.getMessageId());
        intent.putExtra("bus_id", emergencyMessage.getBusId());
        intent.putExtra("student_id", emergencyMessage.getStudentId());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 
                emergencyMessage.getMessageId().hashCode(), 
                intent, 
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Create emergency notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_EMERGENCY)
                .setSmallIcon(R.drawable.ic_bus_logo)
                .setContentTitle("🚨 " + emergencyMessage.getTitle())
                .setContentText(emergencyMessage.getContent())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setFullScreenIntent(pendingIntent, true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                .setVibrate(new long[]{0, 500, 200, 500, 200, 500})
                .setLights(0xFF0000, 1000, 1000);

        // Add emergency actions
        addEmergencyActions(builder, emergencyMessage);

        // Show notification
        notificationManager.notify(emergencyMessage.getMessageId().hashCode(), builder.build());
    }

    /**
     * Show location update notification
     */
    public void showLocationNotification(String busId, String busNumber, String location) {
        Log.d(TAG, "Showing location notification for bus: " + busNumber);

        Intent intent = new Intent(context, MapsActivity.class);
        intent.putExtra("bus_id", busId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 
                busId.hashCode(), 
                intent, 
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_LOCATION)
                .setSmallIcon(R.drawable.ic_bus_logo)
                .setContentTitle("Bus " + busNumber + " Location Update")
                .setContentText("Current location: " + location)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setCategory(NotificationCompat.CATEGORY_TRANSPORT);

        notificationManager.notify(busId.hashCode(), builder.build());
    }

    /**
     * Show check-in confirmation notification
     */
    public void showCheckInNotification(String studentId, String stopName, String checkInType) {
        Log.d(TAG, "Showing check-in notification for student: " + studentId);

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("show_checkin", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 
                studentId.hashCode(), 
                intent, 
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_CROSS_APP)
                .setSmallIcon(R.drawable.ic_bus_logo)
                .setContentTitle("Check-in Confirmed")
                .setContentText("Successfully checked in at " + stopName + " via " + checkInType)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_STATUS);

        notificationManager.notify(studentId.hashCode(), builder.build());
    }

    /**
     * Cancel notification by ID
     */
    public void cancelNotification(String notificationId) {
        notificationManager.cancel(notificationId.hashCode());
    }

    /**
     * Cancel all notifications
     */
    public void cancelAllNotifications() {
        notificationManager.cancelAll();
    }

    // Private helper methods
    private String getChannelIdForMessage(CrossAppMessage message) {
        switch (message.getMessageType()) {
            case CrossAppIntegrationService.MESSAGE_TYPE_EMERGENCY:
                return CHANNEL_ID_EMERGENCY;
            case CrossAppIntegrationService.MESSAGE_TYPE_LOCATION_UPDATE:
                return CHANNEL_ID_LOCATION;
            default:
                return CHANNEL_ID_CROSS_APP;
        }
    }

    private int getNotificationPriority(String priority) {
        switch (priority) {
            case CrossAppIntegrationService.PRIORITY_URGENT:
                return NotificationCompat.PRIORITY_MAX;
            case CrossAppIntegrationService.PRIORITY_HIGH:
                return NotificationCompat.PRIORITY_HIGH;
            case CrossAppIntegrationService.PRIORITY_MEDIUM:
                return NotificationCompat.PRIORITY_DEFAULT;
            case CrossAppIntegrationService.PRIORITY_LOW:
                return NotificationCompat.PRIORITY_LOW;
            default:
                return NotificationCompat.PRIORITY_DEFAULT;
        }
    }

    private String getNotificationCategory(String messageType) {
        switch (messageType) {
            case CrossAppIntegrationService.MESSAGE_TYPE_EMERGENCY:
                return NotificationCompat.CATEGORY_ALARM;
            case CrossAppIntegrationService.MESSAGE_TYPE_LOCATION_UPDATE:
                return NotificationCompat.CATEGORY_TRANSPORT;
            case CrossAppIntegrationService.MESSAGE_TYPE_CHECKIN:
                return NotificationCompat.CATEGORY_STATUS;
            default:
                return NotificationCompat.CATEGORY_MESSAGE;
        }
    }

    private Intent createIntentForMessage(CrossAppMessage message) {
        Intent intent;
        
        switch (message.getMessageType()) {
            case CrossAppIntegrationService.MESSAGE_TYPE_LOCATION_UPDATE:
                intent = new Intent(context, MapsActivity.class);
                if (message.getBusId() != null) {
                    intent.putExtra("bus_id", message.getBusId());
                }
                break;
            case CrossAppIntegrationService.MESSAGE_TYPE_EMERGENCY:
                intent = new Intent(context, MapsActivity.class);
                intent.putExtra("emergency", true);
                intent.putExtra("message_id", message.getMessageId());
                break;
            default:
                intent = new Intent(context, MainActivity.class);
                intent.putExtra("message_id", message.getMessageId());
                break;
        }
        
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    private void addNotificationActions(NotificationCompat.Builder builder, CrossAppMessage message) {
        // Add "Mark as Read" action
        Intent markReadIntent = new Intent(context, CrossAppNotificationReceiver.class);
        markReadIntent.setAction("MARK_AS_READ");
        markReadIntent.putExtra("message_id", message.getMessageId());
        
        PendingIntent markReadPendingIntent = PendingIntent.getBroadcast(
                context, 
                message.getMessageId().hashCode(), 
                markReadIntent, 
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        builder.addAction(R.drawable.ic_bus_logo, "Mark as Read", markReadPendingIntent);

        // Add specific actions based on message type
        if (message.getMessageType().equals(CrossAppIntegrationService.MESSAGE_TYPE_LOCATION_UPDATE)) {
            Intent viewMapIntent = new Intent(context, MapsActivity.class);
            if (message.getBusId() != null) {
                viewMapIntent.putExtra("bus_id", message.getBusId());
            }
            
            PendingIntent viewMapPendingIntent = PendingIntent.getActivity(
                    context, 
                    message.getMessageId().hashCode() + 1, 
                    viewMapIntent, 
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );
            
            builder.addAction(R.drawable.ic_bus_logo, "View Map", viewMapPendingIntent);
        }
    }

    private void addEmergencyActions(NotificationCompat.Builder builder, CrossAppMessage emergencyMessage) {
        // Add "Call Emergency" action
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        // You would set the emergency contact number here
        callIntent.setData(Uri.parse("tel:911"));
        
        PendingIntent callPendingIntent = PendingIntent.getActivity(
                context, 
                emergencyMessage.getMessageId().hashCode() + 2, 
                callIntent, 
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        builder.addAction(R.drawable.ic_bus_logo, "Call Emergency", callPendingIntent);

        // Add "View Location" action
        Intent locationIntent = new Intent(context, MapsActivity.class);
        locationIntent.putExtra("emergency", true);
        locationIntent.putExtra("bus_id", emergencyMessage.getBusId());
        
        PendingIntent locationPendingIntent = PendingIntent.getActivity(
                context, 
                emergencyMessage.getMessageId().hashCode() + 3, 
                locationIntent, 
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        builder.addAction(R.drawable.ic_bus_logo, "View Location", locationPendingIntent);
    }
}
