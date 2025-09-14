package com.example.student.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.student.R;
import com.example.student.models.CrossAppMessage;
import com.example.student.models.Bus;
import com.example.student.models.Student;
import com.example.student.ui.MainActivity;
import com.example.student.ui.maps.MapsActivity;
import com.example.student.ui.checkin.CheckInActivity;
import com.example.student.ui.notifications.NotificationsActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Enhanced notification manager for cross-app communication
 * Handles different types of notifications with rich content and actions
 */
public class EnhancedNotificationManager {
    private static final String TAG = "EnhancedNotificationManager";
    
    // Notification channels
    private static final String CHANNEL_ID_EMERGENCY = "emergency_notifications";
    private static final String CHANNEL_ID_DRIVER_MESSAGE = "driver_messages";
    private static final String CHANNEL_ID_SUPERVISOR_MESSAGE = "supervisor_messages";
    private static final String CHANNEL_ID_BUS_UPDATE = "bus_updates";
    private static final String CHANNEL_ID_CHECKIN = "checkin_notifications";
    private static final String CHANNEL_ID_SCHEDULE = "schedule_notifications";
    
    // Notification IDs
    private static final int NOTIFICATION_ID_EMERGENCY = 1000;
    private static final int NOTIFICATION_ID_DRIVER_MESSAGE = 2000;
    private static final int NOTIFICATION_ID_SUPERVISOR_MESSAGE = 3000;
    private static final int NOTIFICATION_ID_BUS_UPDATE = 4000;
    private static final int NOTIFICATION_ID_CHECKIN = 5000;
    private static final int NOTIFICATION_ID_SCHEDULE = 6000;
    
    private Context context;
    private NotificationManagerCompat notificationManager;
    private CrossAppNotificationService crossAppNotificationService;

    public EnhancedNotificationManager(Context context) {
        this.context = context.getApplicationContext();
        this.notificationManager = NotificationManagerCompat.from(context);
        this.crossAppNotificationService = new CrossAppNotificationService(context);
        createNotificationChannels();
    }

    /**
     * Create notification channels for different types of notifications
     */
    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            
            // Emergency notifications
            NotificationChannel emergencyChannel = new NotificationChannel(
                    CHANNEL_ID_EMERGENCY,
                    "Emergency Alerts",
                    NotificationManager.IMPORTANCE_HIGH
            );
            emergencyChannel.setDescription("Critical emergency notifications from drivers and supervisors");
            emergencyChannel.enableLights(true);
            emergencyChannel.enableVibration(true);
            emergencyChannel.setShowBadge(true);
            emergencyChannel.setLockscreenVisibility(android.app.Notification.VISIBILITY_PUBLIC);
            manager.createNotificationChannel(emergencyChannel);

            // Driver messages
            NotificationChannel driverChannel = new NotificationChannel(
                    CHANNEL_ID_DRIVER_MESSAGE,
                    "Driver Messages",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            driverChannel.setDescription("Messages and updates from your bus driver");
            driverChannel.enableLights(true);
            driverChannel.enableVibration(true);
            manager.createNotificationChannel(driverChannel);

            // Supervisor messages
            NotificationChannel supervisorChannel = new NotificationChannel(
                    CHANNEL_ID_SUPERVISOR_MESSAGE,
                    "Supervisor Messages",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            supervisorChannel.setDescription("Messages and updates from supervisors");
            supervisorChannel.enableLights(true);
            supervisorChannel.enableVibration(true);
            manager.createNotificationChannel(supervisorChannel);

            // Bus updates
            NotificationChannel busChannel = new NotificationChannel(
                    CHANNEL_ID_BUS_UPDATE,
                    "Bus Updates",
                    NotificationManager.IMPORTANCE_LOW
            );
            busChannel.setDescription("Bus location and status updates");
            busChannel.enableLights(false);
            busChannel.enableVibration(false);
            manager.createNotificationChannel(busChannel);

            // Check-in notifications
            NotificationChannel checkinChannel = new NotificationChannel(
                    CHANNEL_ID_CHECKIN,
                    "Check-in Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            checkinChannel.setDescription("Check-in confirmations and reminders");
            checkinChannel.enableLights(true);
            checkinChannel.enableVibration(true);
            manager.createNotificationChannel(checkinChannel);

            // Schedule notifications
            NotificationChannel scheduleChannel = new NotificationChannel(
                    CHANNEL_ID_SCHEDULE,
                    "Schedule Updates",
                    NotificationManager.IMPORTANCE_LOW
            );
            scheduleChannel.setDescription("Schedule changes and updates");
            scheduleChannel.enableLights(false);
            scheduleChannel.enableVibration(false);
            manager.createNotificationChannel(scheduleChannel);
        }
    }

    /**
     * Show emergency notification with full-screen intent
     */
    public void showEmergencyNotification(CrossAppMessage message) {
        Log.d(TAG, "Showing emergency notification: " + message.getTitle());
        
        Intent intent = createEmergencyIntent(message);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 
                message.getMessageId().hashCode(), 
                intent, 
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_EMERGENCY)
                .setSmallIcon(R.drawable.ic_bus_logo)
                .setLargeIcon(getLargeIcon(R.drawable.ic_bus_logo))
                .setContentTitle("🚨 " + message.getTitle())
                .setContentText(message.getContent())
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message.getContent()))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setFullScreenIntent(pendingIntent, true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                .setVibrate(new long[]{0, 500, 200, 500, 200, 500})
                .setLights(0xFF0000, 1000, 1000)
                .setOngoing(true);

        // Add emergency actions
        addEmergencyActions(builder, message);

        notificationManager.notify(NOTIFICATION_ID_EMERGENCY, builder.build());
    }

    /**
     * Show driver message notification
     */
    public void showDriverMessageNotification(CrossAppMessage message) {
        Log.d(TAG, "Showing driver message notification: " + message.getTitle());
        
        Intent intent = createMessageIntent(message);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 
                message.getMessageId().hashCode(), 
                intent, 
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_DRIVER_MESSAGE)
                .setSmallIcon(R.drawable.ic_bus_logo)
                .setLargeIcon(getLargeIcon(R.drawable.ic_bus_logo))
                .setContentTitle("👨‍💼 " + message.getTitle())
                .setContentText(message.getContent())
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message.getContent()))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE);

        // Add message actions
        addMessageActions(builder, message);

        notificationManager.notify(NOTIFICATION_ID_DRIVER_MESSAGE + message.getMessageId().hashCode(), builder.build());
    }

    /**
     * Show supervisor message notification
     */
    public void showSupervisorMessageNotification(CrossAppMessage message) {
        Log.d(TAG, "Showing supervisor message notification: " + message.getTitle());
        
        Intent intent = createMessageIntent(message);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 
                message.getMessageId().hashCode(), 
                intent, 
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_SUPERVISOR_MESSAGE)
                .setSmallIcon(R.drawable.ic_bus_logo)
                .setLargeIcon(getLargeIcon(R.drawable.ic_bus_logo))
                .setContentTitle("👨‍💼 " + message.getTitle())
                .setContentText(message.getContent())
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message.getContent()))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE);

        // Add message actions
        addMessageActions(builder, message);

        notificationManager.notify(NOTIFICATION_ID_SUPERVISOR_MESSAGE + message.getMessageId().hashCode(), builder.build());
    }

    /**
     * Show bus update notification
     */
    public void showBusUpdateNotification(Bus bus, String updateType) {
        Log.d(TAG, "Showing bus update notification: " + updateType);
        
        Intent intent = new Intent(context, MapsActivity.class);
        intent.putExtra("bus_id", bus.getBusId());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 
                bus.getBusId().hashCode(), 
                intent, 
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        String title = "🚌 Bus " + bus.getBusNumber() + " Update";
        String content = getBusUpdateContent(bus, updateType);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_BUS_UPDATE)
                .setSmallIcon(R.drawable.ic_bus_logo)
                .setLargeIcon(getLargeIcon(R.drawable.ic_bus_logo))
                .setContentTitle(title)
                .setContentText(content)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(content))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setCategory(NotificationCompat.CATEGORY_TRANSPORT);

        notificationManager.notify(NOTIFICATION_ID_BUS_UPDATE + bus.getBusId().hashCode(), builder.build());
    }

    /**
     * Show check-in notification
     */
    public void showCheckInNotification(String studentId, String stopName, String checkInType, boolean success) {
        Log.d(TAG, "Showing check-in notification: " + checkInType);
        
        Intent intent = new Intent(context, CheckInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 
                studentId.hashCode(), 
                intent, 
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        String title = success ? "✅ Check-in Successful" : "❌ Check-in Failed";
        String content = success ? 
                "Successfully checked in at " + stopName + " via " + checkInType :
                "Failed to check in at " + stopName + ". Please try again.";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_CHECKIN)
                .setSmallIcon(R.drawable.ic_bus_logo)
                .setLargeIcon(getLargeIcon(R.drawable.ic_bus_logo))
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_STATUS);

        notificationManager.notify(NOTIFICATION_ID_CHECKIN + studentId.hashCode(), builder.build());
    }

    /**
     * Show schedule update notification
     */
    public void showScheduleUpdateNotification(String routeName, String updateDetails) {
        Log.d(TAG, "Showing schedule update notification for route: " + routeName);
        
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("show_schedule", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 
                routeName.hashCode(), 
                intent, 
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_SCHEDULE)
                .setSmallIcon(R.drawable.ic_bus_logo)
                .setLargeIcon(getLargeIcon(R.drawable.ic_bus_logo))
                .setContentTitle("📅 Schedule Update - " + routeName)
                .setContentText(updateDetails)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(updateDetails))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setCategory(NotificationCompat.CATEGORY_TRANSPORT);

        notificationManager.notify(NOTIFICATION_ID_SCHEDULE + routeName.hashCode(), builder.build());
    }

    /**
     * Show grouped notifications for multiple messages
     */
    public void showGroupedNotifications(List<CrossAppMessage> messages, String groupKey) {
        Log.d(TAG, "Showing grouped notifications: " + messages.size() + " messages");
        
        // Create summary notification
        Intent intent = new Intent(context, NotificationsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 
                groupKey.hashCode(), 
                intent, 
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder summaryBuilder = new NotificationCompat.Builder(context, CHANNEL_ID_DRIVER_MESSAGE)
                .setSmallIcon(R.drawable.ic_bus_logo)
                .setContentTitle("New Messages (" + messages.size() + ")")
                .setContentText("You have " + messages.size() + " new messages")
                .setGroupSummary(true)
                .setGroup(groupKey)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        notificationManager.notify(groupKey.hashCode(), summaryBuilder.build());

        // Create individual notifications
        for (int i = 0; i < messages.size(); i++) {
            CrossAppMessage message = messages.get(i);
            showDriverMessageNotification(message);
        }
    }

    /**
     * Cancel notification by ID
     */
    public void cancelNotification(int notificationId) {
        notificationManager.cancel(notificationId);
    }

    /**
     * Cancel all notifications
     */
    public void cancelAllNotifications() {
        notificationManager.cancelAll();
    }

    // Private helper methods
    private Intent createEmergencyIntent(CrossAppMessage message) {
        Intent intent = new Intent(context, MapsActivity.class);
        intent.putExtra("emergency", true);
        intent.putExtra("message_id", message.getMessageId());
        intent.putExtra("bus_id", message.getBusId());
        intent.putExtra("student_id", message.getStudentId());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    private Intent createMessageIntent(CrossAppMessage message) {
        Intent intent = new Intent(context, NotificationsActivity.class);
        intent.putExtra("message_id", message.getMessageId());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    private Bitmap getLargeIcon(int iconRes) {
        return BitmapFactory.decodeResource(context.getResources(), iconRes);
    }

    private String getBusUpdateContent(Bus bus, String updateType) {
        switch (updateType) {
            case "location":
                return "Bus is at " + (bus.getCurrentLocation() != null ? 
                        "Lat: " + bus.getCurrentLocation().latitude + ", Lng: " + bus.getCurrentLocation().longitude : 
                        "Unknown location");
            case "status":
                return "Status: " + bus.getStatus() + ", Moving: " + bus.isMoving();
            case "delay":
                return "Bus is delayed. Estimated arrival: " + bus.getLastUpdated();
            case "on_route":
                return "Bus is now on route " + bus.getRouteId();
            default:
                return "Bus " + bus.getBusNumber() + " update received";
        }
    }

    private void addEmergencyActions(NotificationCompat.Builder builder, CrossAppMessage message) {
        // Call emergency action
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:911"));
        
        PendingIntent callPendingIntent = PendingIntent.getActivity(
                context, 
                message.getMessageId().hashCode() + 1, 
                callIntent, 
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        builder.addAction(R.drawable.ic_bus_logo, "Call Emergency", callPendingIntent);

        // View location action
        Intent locationIntent = new Intent(context, MapsActivity.class);
        locationIntent.putExtra("emergency", true);
        locationIntent.putExtra("bus_id", message.getBusId());
        
        PendingIntent locationPendingIntent = PendingIntent.getActivity(
                context, 
                message.getMessageId().hashCode() + 2, 
                locationIntent, 
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        builder.addAction(R.drawable.ic_bus_logo, "View Location", locationPendingIntent);
    }

    private void addMessageActions(NotificationCompat.Builder builder, CrossAppMessage message) {
        // Mark as read action
        Intent markReadIntent = new Intent(context, com.example.student.receivers.CrossAppNotificationReceiver.class);
        markReadIntent.setAction("MARK_AS_READ");
        markReadIntent.putExtra("message_id", message.getMessageId());
        
        PendingIntent markReadPendingIntent = PendingIntent.getBroadcast(
                context, 
                message.getMessageId().hashCode() + 3, 
                markReadIntent, 
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        builder.addAction(R.drawable.ic_bus_logo, "Mark as Read", markReadPendingIntent);

        // Reply action (if supported)
        Intent replyIntent = new Intent(context, NotificationsActivity.class);
        replyIntent.putExtra("reply_to", message.getSenderId());
        replyIntent.putExtra("message_id", message.getMessageId());
        
        PendingIntent replyPendingIntent = PendingIntent.getActivity(
                context, 
                message.getMessageId().hashCode() + 4, 
                replyIntent, 
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        builder.addAction(R.drawable.ic_bus_logo, "Reply", replyPendingIntent);
    }
}
