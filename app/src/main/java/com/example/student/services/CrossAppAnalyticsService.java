package com.example.student.services;

import android.content.Context;
import android.util.Log;

import com.example.student.models.CrossAppMessage;
import com.example.student.models.Student;
import com.example.student.models.Bus;
import com.example.student.utils.Constants;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Analytics service for tracking cross-app communication and performance metrics
 */
public class CrossAppAnalyticsService {
    private static final String TAG = "CrossAppAnalytics";
    private static CrossAppAnalyticsService instance;
    
    private FirebaseAnalytics firebaseAnalytics;
    private FirebaseFirestore firestore;
    private Context context;
    
    // Analytics events
    public static final String EVENT_MESSAGE_SENT = "cross_app_message_sent";
    public static final String EVENT_MESSAGE_RECEIVED = "cross_app_message_received";
    public static final String EVENT_EMERGENCY_TRIGGERED = "emergency_alert_triggered";
    public static final String EVENT_LOCATION_UPDATE_SENT = "location_update_sent";
    public static final String EVENT_CHECKIN_NOTIFICATION_SENT = "checkin_notification_sent";
    public static final String EVENT_NOTIFICATION_SHOWN = "notification_shown";
    public static final String EVENT_NOTIFICATION_CLICKED = "notification_clicked";
    public static final String EVENT_BUS_STATUS_UPDATED = "bus_status_updated";
    public static final String EVENT_SYNC_PERFORMED = "data_sync_performed";
    public static final String EVENT_ERROR_OCCURRED = "cross_app_error_occurred";
    
    // Analytics parameters
    public static final String PARAM_MESSAGE_TYPE = "message_type";
    public static final String PARAM_SENDER_TYPE = "sender_type";
    public static final String PARAM_RECEIVER_TYPE = "receiver_type";
    public static final String PARAM_PRIORITY = "priority";
    public static final String PARAM_BUS_ID = "bus_id";
    public static final String PARAM_ROUTE_ID = "route_id";
    public static final String PARAM_STUDENT_ID = "student_id";
    public static final String PARAM_EMERGENCY_TYPE = "emergency_type";
    public static final String PARAM_CHECKIN_TYPE = "checkin_type";
    public static final String PARAM_NOTIFICATION_TYPE = "notification_type";
    public static final String PARAM_ERROR_CODE = "error_code";
    public static final String PARAM_ERROR_MESSAGE = "error_message";
    public static final String PARAM_SYNC_TYPE = "sync_type";
    public static final String PARAM_SYNC_DURATION = "sync_duration_ms";
    public static final String PARAM_MESSAGE_COUNT = "message_count";
    public static final String PARAM_RESPONSE_TIME = "response_time_ms";

    private CrossAppAnalyticsService(Context context) {
        this.context = context.getApplicationContext();
        this.firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        this.firestore = FirebaseFirestore.getInstance();
    }

    public static synchronized CrossAppAnalyticsService getInstance(Context context) {
        if (instance == null) {
            instance = new CrossAppAnalyticsService(context);
        }
        return instance;
    }

    /**
     * Track message sent event
     */
    public void trackMessageSent(CrossAppMessage message) {
        Map<String, Object> params = new HashMap<>();
        params.put(PARAM_MESSAGE_TYPE, message.getMessageType());
        params.put(PARAM_SENDER_TYPE, message.getSenderType());
        params.put(PARAM_RECEIVER_TYPE, message.getReceiverType());
        params.put(PARAM_PRIORITY, message.getPriority());
        params.put(PARAM_BUS_ID, message.getBusId());
        params.put(PARAM_ROUTE_ID, message.getRouteId());
        params.put(PARAM_STUDENT_ID, message.getStudentId());
        
        firebaseAnalytics.logEvent(EVENT_MESSAGE_SENT, createBundle(params));
        Log.d(TAG, "Tracked message sent: " + message.getMessageType());
    }

    /**
     * Track message received event
     */
    public void trackMessageReceived(CrossAppMessage message) {
        Map<String, Object> params = new HashMap<>();
        params.put(PARAM_MESSAGE_TYPE, message.getMessageType());
        params.put(PARAM_SENDER_TYPE, message.getSenderType());
        params.put(PARAM_RECEIVER_TYPE, message.getReceiverType());
        params.put(PARAM_PRIORITY, message.getPriority());
        params.put(PARAM_BUS_ID, message.getBusId());
        params.put(PARAM_ROUTE_ID, message.getRouteId());
        params.put(PARAM_STUDENT_ID, message.getStudentId());
        
        firebaseAnalytics.logEvent(EVENT_MESSAGE_RECEIVED, createBundle(params));
        Log.d(TAG, "Tracked message received: " + message.getMessageType());
    }

    /**
     * Track emergency alert triggered
     */
    public void trackEmergencyTriggered(String studentId, String busId, String emergencyType, String message) {
        Map<String, Object> params = new HashMap<>();
        params.put(PARAM_STUDENT_ID, studentId);
        params.put(PARAM_BUS_ID, busId);
        params.put(PARAM_EMERGENCY_TYPE, emergencyType);
        params.put(PARAM_PRIORITY, CrossAppIntegrationService.PRIORITY_URGENT);
        
        firebaseAnalytics.logEvent(EVENT_EMERGENCY_TRIGGERED, createBundle(params));
        Log.d(TAG, "Tracked emergency triggered: " + emergencyType);
    }

    /**
     * Track location update sent
     */
    public void trackLocationUpdateSent(String studentId, String busId, String routeId, 
                                       double latitude, double longitude, double speed) {
        Map<String, Object> params = new HashMap<>();
        params.put(PARAM_STUDENT_ID, studentId);
        params.put(PARAM_BUS_ID, busId);
        params.put(PARAM_ROUTE_ID, routeId);
        params.put("latitude", latitude);
        params.put("longitude", longitude);
        params.put("speed", speed);
        
        firebaseAnalytics.logEvent(EVENT_LOCATION_UPDATE_SENT, createBundle(params));
        Log.d(TAG, "Tracked location update sent for bus: " + busId);
    }

    /**
     * Track check-in notification sent
     */
    public void trackCheckInNotificationSent(String studentId, String busId, String stopId, String checkInType) {
        Map<String, Object> params = new HashMap<>();
        params.put(PARAM_STUDENT_ID, studentId);
        params.put(PARAM_BUS_ID, busId);
        params.put(PARAM_CHECKIN_TYPE, checkInType);
        params.put("stop_id", stopId);
        
        firebaseAnalytics.logEvent(EVENT_CHECKIN_NOTIFICATION_SENT, createBundle(params));
        Log.d(TAG, "Tracked check-in notification sent: " + checkInType);
    }

    /**
     * Track notification shown
     */
    public void trackNotificationShown(String notificationType, String messageId, String priority) {
        Map<String, Object> params = new HashMap<>();
        params.put(PARAM_NOTIFICATION_TYPE, notificationType);
        params.put("message_id", messageId);
        params.put(PARAM_PRIORITY, priority);
        
        firebaseAnalytics.logEvent(EVENT_NOTIFICATION_SHOWN, createBundle(params));
        Log.d(TAG, "Tracked notification shown: " + notificationType);
    }

    /**
     * Track notification clicked
     */
    public void trackNotificationClicked(String notificationType, String messageId, String action) {
        Map<String, Object> params = new HashMap<>();
        params.put(PARAM_NOTIFICATION_TYPE, notificationType);
        params.put("message_id", messageId);
        params.put("action", action);
        
        firebaseAnalytics.logEvent(EVENT_NOTIFICATION_CLICKED, createBundle(params));
        Log.d(TAG, "Tracked notification clicked: " + notificationType);
    }

    /**
     * Track bus status updated
     */
    public void trackBusStatusUpdated(String busId, String status, boolean isOnRoute, boolean isMoving) {
        Map<String, Object> params = new HashMap<>();
        params.put(PARAM_BUS_ID, busId);
        params.put("status", status);
        params.put("is_on_route", isOnRoute);
        params.put("is_moving", isMoving);
        
        firebaseAnalytics.logEvent(EVENT_BUS_STATUS_UPDATED, createBundle(params));
        Log.d(TAG, "Tracked bus status updated: " + status);
    }

    /**
     * Track data sync performed
     */
    public void trackDataSyncPerformed(String syncType, long durationMs, int recordCount) {
        Map<String, Object> params = new HashMap<>();
        params.put(PARAM_SYNC_TYPE, syncType);
        params.put(PARAM_SYNC_DURATION, durationMs);
        params.put("record_count", recordCount);
        
        firebaseAnalytics.logEvent(EVENT_SYNC_PERFORMED, createBundle(params));
        Log.d(TAG, "Tracked data sync performed: " + syncType + " (" + durationMs + "ms)");
    }

    /**
     * Track error occurred
     */
    public void trackErrorOccurred(String errorCode, String errorMessage, String component) {
        Map<String, Object> params = new HashMap<>();
        params.put(PARAM_ERROR_CODE, errorCode);
        params.put(PARAM_ERROR_MESSAGE, errorMessage);
        params.put("component", component);
        
        firebaseAnalytics.logEvent(EVENT_ERROR_OCCURRED, createBundle(params));
        Log.d(TAG, "Tracked error occurred: " + errorCode + " - " + errorMessage);
    }

    /**
     * Track user engagement metrics
     */
    public void trackUserEngagement(String studentId, String action, String details) {
        Map<String, Object> params = new HashMap<>();
        params.put(PARAM_STUDENT_ID, studentId);
        params.put("action", action);
        params.put("details", details);
        
        firebaseAnalytics.logEvent("user_engagement", createBundle(params));
        Log.d(TAG, "Tracked user engagement: " + action);
    }

    /**
     * Track app performance metrics
     */
    public void trackAppPerformance(String metric, long value, String unit) {
        Map<String, Object> params = new HashMap<>();
        params.put("metric", metric);
        params.put("value", value);
        params.put("unit", unit);
        
        firebaseAnalytics.logEvent("app_performance", createBundle(params));
        Log.d(TAG, "Tracked app performance: " + metric + " = " + value + " " + unit);
    }

    /**
     * Track cross-app communication success rate
     */
    public void trackCommunicationSuccessRate(String messageType, boolean success, long responseTime) {
        Map<String, Object> params = new HashMap<>();
        params.put(PARAM_MESSAGE_TYPE, messageType);
        params.put("success", success);
        params.put(PARAM_RESPONSE_TIME, responseTime);
        
        firebaseAnalytics.logEvent("communication_success_rate", createBundle(params));
        Log.d(TAG, "Tracked communication success rate: " + messageType + " = " + success);
    }

    /**
     * Store detailed analytics data in Firestore
     */
    public CompletableFuture<Boolean> storeAnalyticsData(String eventType, Map<String, Object> data) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        try {
            Map<String, Object> analyticsData = new HashMap<>(data);
            analyticsData.put("event_type", eventType);
            analyticsData.put("timestamp", System.currentTimeMillis());
            analyticsData.put("app_version", getAppVersion());
            analyticsData.put("device_info", getDeviceInfo());
            
            firestore.collection("analytics")
                    .add(analyticsData)
                    .addOnSuccessListener(documentReference -> {
                        Log.d(TAG, "Analytics data stored: " + documentReference.getId());
                        future.complete(true);
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error storing analytics data", e);
                        future.complete(false);
                    });
        } catch (Exception e) {
            Log.e(TAG, "Error creating analytics data", e);
            future.complete(false);
        }
        
        return future;
    }

    /**
     * Get analytics summary for a student
     */
    public CompletableFuture<Map<String, Object>> getAnalyticsSummary(String studentId) {
        CompletableFuture<Map<String, Object>> future = new CompletableFuture<>();
        
        firestore.collection("analytics")
                .whereEqualTo("student_id", studentId)
                .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(100)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    Map<String, Object> summary = new HashMap<>();
                    summary.put("total_events", querySnapshot.size());
                    summary.put("last_updated", System.currentTimeMillis());
                    
                    // Calculate message statistics
                    int messageCount = 0;
                    int emergencyCount = 0;
                    int locationUpdateCount = 0;
                    
                    for (com.google.firebase.firestore.DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        String eventType = doc.getString("event_type");
                        if (eventType != null) {
                            switch (eventType) {
                                case EVENT_MESSAGE_SENT:
                                case EVENT_MESSAGE_RECEIVED:
                                    messageCount++;
                                    break;
                                case EVENT_EMERGENCY_TRIGGERED:
                                    emergencyCount++;
                                    break;
                                case EVENT_LOCATION_UPDATE_SENT:
                                    locationUpdateCount++;
                                    break;
                            }
                        }
                    }
                    
                    summary.put("message_count", messageCount);
                    summary.put("emergency_count", emergencyCount);
                    summary.put("location_update_count", locationUpdateCount);
                    
                    future.complete(summary);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error getting analytics summary", e);
                    future.complete(new HashMap<>());
                });
        
        return future;
    }

    // Private helper methods
    private android.os.Bundle createBundle(Map<String, Object> params) {
        android.os.Bundle bundle = new android.os.Bundle();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (entry.getValue() instanceof String) {
                bundle.putString(entry.getKey(), (String) entry.getValue());
            } else if (entry.getValue() instanceof Long) {
                bundle.putLong(entry.getKey(), (Long) entry.getValue());
            } else if (entry.getValue() instanceof Integer) {
                bundle.putInt(entry.getKey(), (Integer) entry.getValue());
            } else if (entry.getValue() instanceof Double) {
                bundle.putDouble(entry.getKey(), (Double) entry.getValue());
            } else if (entry.getValue() instanceof Boolean) {
                bundle.putBoolean(entry.getKey(), (Boolean) entry.getValue());
            }
        }
        return bundle;
    }

    private String getAppVersion() {
        try {
            return context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0)
                    .versionName;
        } catch (Exception e) {
            return "unknown";
        }
    }

    private Map<String, Object> getDeviceInfo() {
        Map<String, Object> deviceInfo = new HashMap<>();
        deviceInfo.put("manufacturer", android.os.Build.MANUFACTURER);
        deviceInfo.put("model", android.os.Build.MODEL);
        deviceInfo.put("android_version", android.os.Build.VERSION.RELEASE);
        deviceInfo.put("sdk_version", android.os.Build.VERSION.SDK_INT);
        return deviceInfo;
    }
}
