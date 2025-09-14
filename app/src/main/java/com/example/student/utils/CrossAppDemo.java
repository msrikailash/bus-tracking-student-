package com.example.student.utils;

import android.content.Context;
import android.util.Log;

import com.example.student.models.CrossAppMessage;
import com.example.student.services.CrossAppIntegrationService;
import com.example.student.services.CrossAppNotificationService;

import java.util.concurrent.CompletableFuture;

/**
 * Demo class to test cross-app integration functionality
 * This class demonstrates how the student app communicates with driver and supervisor apps
 */
public class CrossAppDemo {
    private static final String TAG = "CrossAppDemo";
    
    private Context context;
    private CrossAppIntegrationService integrationService;
    private CrossAppNotificationService notificationService;

    public CrossAppDemo(Context context) {
        this.context = context;
        this.integrationService = CrossAppIntegrationService.getInstance(context);
        this.notificationService = new CrossAppNotificationService(context);
    }

    /**
     * Demo: Send a test location update to driver and supervisor
     */
    public void demoLocationUpdate() {
        Log.d(TAG, "Demo: Sending location update");
        
        String studentId = "demo001";
        String busId = "BUS001";
        double latitude = 37.7749;
        double longitude = -122.4194;
        double speed = 25.5;
        String direction = "North";
        
        integrationService.sendLocationUpdate(studentId, busId, latitude, longitude, speed, direction)
                .thenAccept(success -> {
                    if (success) {
                        Log.d(TAG, "Demo: Location update sent successfully");
                    } else {
                        Log.e(TAG, "Demo: Failed to send location update");
                    }
                });
    }

    /**
     * Demo: Send a test check-in notification
     */
    public void demoCheckInNotification() {
        Log.d(TAG, "Demo: Sending check-in notification");
        
        String studentId = "demo001";
        String busId = "BUS001";
        String stopId = "STOP001";
        String checkInType = "QR";
        
        integrationService.sendCheckInNotification(studentId, busId, stopId, checkInType)
                .thenAccept(success -> {
                    if (success) {
                        Log.d(TAG, "Demo: Check-in notification sent successfully");
                    } else {
                        Log.e(TAG, "Demo: Failed to send check-in notification");
                    }
                });
    }

    /**
     * Demo: Send a test emergency alert
     */
    public void demoEmergencyAlert() {
        Log.d(TAG, "Demo: Sending emergency alert");
        
        String studentId = "demo001";
        String busId = "BUS001";
        String emergencyType = "Medical Emergency";
        String message = "Student requires immediate medical attention";
        
        integrationService.sendEmergencyAlert(studentId, busId, emergencyType, message)
                .thenAccept(success -> {
                    if (success) {
                        Log.d(TAG, "Demo: Emergency alert sent successfully");
                    } else {
                        Log.e(TAG, "Demo: Failed to send emergency alert");
                    }
                });
    }

    /**
     * Demo: Send a test cross-app message
     */
    public void demoCrossAppMessage() {
        Log.d(TAG, "Demo: Sending cross-app message");
        
        String messageId = "demo_msg_" + System.currentTimeMillis();
        String senderId = "demo001";
        String senderType = CrossAppIntegrationService.USER_TYPE_STUDENT;
        String receiverId = "driver001";
        String receiverType = CrossAppIntegrationService.USER_TYPE_DRIVER;
        String messageType = CrossAppIntegrationService.MESSAGE_TYPE_NOTIFICATION;
        
        CrossAppMessage message = new CrossAppMessage(
                messageId, senderId, senderType, receiverId, receiverType, messageType
        );
        
        message.setTitle("Test Message from Student");
        message.setContent("This is a test message from the student app to the driver app");
        message.setPriority(CrossAppIntegrationService.PRIORITY_MEDIUM);
        
        integrationService.sendMessage(message)
                .thenAccept(success -> {
                    if (success) {
                        Log.d(TAG, "Demo: Cross-app message sent successfully");
                    } else {
                        Log.e(TAG, "Demo: Failed to send cross-app message");
                    }
                });
    }

    /**
     * Demo: Show test notification
     */
    public void demoNotification() {
        Log.d(TAG, "Demo: Showing test notification");
        
        String messageId = "demo_notification_" + System.currentTimeMillis();
        String senderId = "driver001";
        String senderType = CrossAppIntegrationService.USER_TYPE_DRIVER;
        String receiverId = "demo001";
        String receiverType = CrossAppIntegrationService.USER_TYPE_STUDENT;
        String messageType = CrossAppIntegrationService.MESSAGE_TYPE_NOTIFICATION;
        
        CrossAppMessage message = new CrossAppMessage(
                messageId, senderId, senderType, receiverId, receiverType, messageType
        );
        
        message.setTitle("Message from Driver");
        message.setContent("Bus will arrive in 5 minutes at your stop");
        message.setPriority(CrossAppIntegrationService.PRIORITY_HIGH);
        message.setBusId("BUS001");
        
        // Show the notification
        notificationService.showCrossAppNotification(message);
        
        Log.d(TAG, "Demo: Test notification displayed");
    }

    /**
     * Demo: Show emergency notification
     */
    public void demoEmergencyNotification() {
        Log.d(TAG, "Demo: Showing emergency notification");
        
        String messageId = "demo_emergency_" + System.currentTimeMillis();
        String senderId = "supervisor001";
        String senderType = CrossAppIntegrationService.USER_TYPE_SUPERVISOR;
        String receiverId = "demo001";
        String receiverType = CrossAppIntegrationService.USER_TYPE_STUDENT;
        String messageType = CrossAppIntegrationService.MESSAGE_TYPE_EMERGENCY;
        
        CrossAppMessage message = new CrossAppMessage(
                messageId, senderId, senderType, receiverId, receiverType, messageType
        );
        
        message.setTitle("EMERGENCY ALERT");
        message.setContent("Bus route has been changed due to road closure. Please check updated route.");
        message.setPriority(CrossAppIntegrationService.PRIORITY_URGENT);
        message.setBusId("BUS001");
        message.setRouteId("ROUTE001");
        
        // Show the emergency notification
        notificationService.showEmergencyNotification(message);
        
        Log.d(TAG, "Demo: Emergency notification displayed");
    }

    /**
     * Run all demo scenarios
     */
    public void runAllDemos() {
        Log.d(TAG, "Running all cross-app integration demos");
        
        // Wait a bit between demos to see the effects
        new Thread(() -> {
            try {
                demoLocationUpdate();
                Thread.sleep(2000);
                
                demoCheckInNotification();
                Thread.sleep(2000);
                
                demoCrossAppMessage();
                Thread.sleep(2000);
                
                demoNotification();
                Thread.sleep(2000);
                
                demoEmergencyAlert();
                Thread.sleep(2000);
                
                demoEmergencyNotification();
                
                Log.d(TAG, "All demos completed");
            } catch (InterruptedException e) {
                Log.e(TAG, "Demo interrupted", e);
            }
        }).start();
    }

    /**
     * Test message listening functionality
     */
    public void testMessageListening() {
        Log.d(TAG, "Testing message listening functionality");
        
        String userId = "demo001";
        String userType = CrossAppIntegrationService.USER_TYPE_STUDENT;
        
        integrationService.startListeningForMessages(userId, userType, new CrossAppIntegrationService.MessageListener() {
            @Override
            public void onMessagesReceived(java.util.List<CrossAppMessage> messages) {
                Log.d(TAG, "Received " + messages.size() + " messages");
                for (CrossAppMessage message : messages) {
                    Log.d(TAG, "Message: " + message.getTitle() + " - " + message.getContent());
                    
                    // Show notification for each message
                    notificationService.showCrossAppNotification(message);
                }
            }
        });
        
        Log.d(TAG, "Message listening started");
    }
}
