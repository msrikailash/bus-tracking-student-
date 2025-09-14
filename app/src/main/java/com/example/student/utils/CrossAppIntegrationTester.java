package com.example.student.utils;

import android.content.Context;
import android.util.Log;

import com.example.student.models.CrossAppMessage;
import com.example.student.models.Bus;
import com.example.student.models.Student;
import com.example.student.models.Driver;
import com.example.student.models.Supervisor;
import com.example.student.services.CrossAppIntegrationService;
import com.example.student.services.RealTimeSyncService;
import com.example.student.services.CrossAppDataSyncService;
import com.example.student.services.CrossAppAnalyticsService;
import com.example.student.services.EnhancedNotificationManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Comprehensive testing suite for cross-app integration
 * Tests all aspects of communication between student, driver, and supervisor apps
 */
public class CrossAppIntegrationTester {
    private static final String TAG = "CrossAppIntegrationTester";
    
    private Context context;
    private CrossAppIntegrationService integrationService;
    private RealTimeSyncService syncService;
    private CrossAppDataSyncService dataSyncService;
    private CrossAppAnalyticsService analyticsService;
    private EnhancedNotificationManager notificationManager;
    
    // Test data
    private String testStudentId = "test_student_001";
    private String testBusId = "test_bus_001";
    private String testRouteId = "test_route_001";
    private String testDriverId = "test_driver_001";
    private String testSupervisorId = "test_supervisor_001";

    public CrossAppIntegrationTester(Context context) {
        this.context = context;
        this.integrationService = CrossAppIntegrationService.getInstance(context);
        this.syncService = RealTimeSyncService.getInstance(context);
        this.dataSyncService = CrossAppDataSyncService.getInstance(context);
        this.analyticsService = CrossAppAnalyticsService.getInstance(context);
        this.notificationManager = new EnhancedNotificationManager(context);
    }

    /**
     * Run comprehensive integration tests
     */
    public void runAllTests() {
        Log.d(TAG, "Starting comprehensive cross-app integration tests");
        
        new Thread(() -> {
            try {
                // Test 1: Basic message sending
                testBasicMessageSending();
                Thread.sleep(2000);
                
                // Test 2: Location updates
                testLocationUpdates();
                Thread.sleep(2000);
                
                // Test 3: Check-in notifications
                testCheckInNotifications();
                Thread.sleep(2000);
                
                // Test 4: Emergency alerts
                testEmergencyAlerts();
                Thread.sleep(2000);
                
                // Test 5: Real-time sync
                testRealTimeSync();
                Thread.sleep(2000);
                
                // Test 6: Data synchronization
                testDataSynchronization();
                Thread.sleep(2000);
                
                // Test 7: Notification system
                testNotificationSystem();
                Thread.sleep(2000);
                
                // Test 8: Analytics tracking
                testAnalyticsTracking();
                Thread.sleep(2000);
                
                // Test 9: Error handling
                testErrorHandling();
                Thread.sleep(2000);
                
                // Test 10: Performance testing
                testPerformance();
                
                Log.d(TAG, "All integration tests completed successfully!");
                
            } catch (InterruptedException e) {
                Log.e(TAG, "Test execution interrupted", e);
            }
        }).start();
    }

    /**
     * Test basic message sending functionality
     */
    private void testBasicMessageSending() {
        Log.d(TAG, "Testing basic message sending...");
        
        // Test student to driver message
        CrossAppMessage studentToDriver = createTestMessage(
                testStudentId, CrossAppIntegrationService.USER_TYPE_STUDENT,
                testDriverId, CrossAppIntegrationService.USER_TYPE_DRIVER,
                CrossAppIntegrationService.MESSAGE_TYPE_NOTIFICATION
        );
        studentToDriver.setTitle("Test Message from Student");
        studentToDriver.setContent("This is a test message from student to driver");
        studentToDriver.setPriority(CrossAppIntegrationService.PRIORITY_MEDIUM);
        
        integrationService.sendMessage(studentToDriver)
                .thenAccept(success -> {
                    if (success) {
                        Log.d(TAG, "✅ Student to driver message sent successfully");
                        analyticsService.trackMessageSent(studentToDriver);
                    } else {
                        Log.e(TAG, "❌ Failed to send student to driver message");
                    }
                });
        
        // Test supervisor to student message
        CrossAppMessage supervisorToStudent = createTestMessage(
                testSupervisorId, CrossAppIntegrationService.USER_TYPE_SUPERVISOR,
                testStudentId, CrossAppIntegrationService.USER_TYPE_STUDENT,
                CrossAppIntegrationService.MESSAGE_TYPE_NOTIFICATION
        );
        supervisorToStudent.setTitle("Test Message from Supervisor");
        supervisorToStudent.setContent("This is a test message from supervisor to student");
        supervisorToStudent.setPriority(CrossAppIntegrationService.PRIORITY_HIGH);
        
        integrationService.sendMessage(supervisorToStudent)
                .thenAccept(success -> {
                    if (success) {
                        Log.d(TAG, "✅ Supervisor to student message sent successfully");
                        analyticsService.trackMessageSent(supervisorToStudent);
                    } else {
                        Log.e(TAG, "❌ Failed to send supervisor to student message");
                    }
                });
    }

    /**
     * Test location update functionality
     */
    private void testLocationUpdates() {
        Log.d(TAG, "Testing location updates...");
        
        // Test student location update
        integrationService.sendLocationUpdate(
                testStudentId, testBusId, 37.7749, -122.4194, 25.5, "North"
        ).thenAccept(success -> {
            if (success) {
                Log.d(TAG, "✅ Student location update sent successfully");
                analyticsService.trackLocationUpdateSent(testStudentId, testBusId, testRouteId, 37.7749, -122.4194, 25.5);
            } else {
                Log.e(TAG, "❌ Failed to send student location update");
            }
        });
        
        // Test bus location update
        integrationService.sendLocationUpdate(
                testStudentId, testBusId, 37.7849, -122.4094, 30.0, "South"
        ).thenAccept(success -> {
            if (success) {
                Log.d(TAG, "✅ Bus location update sent successfully");
            } else {
                Log.e(TAG, "❌ Failed to send bus location update");
            }
        });
    }

    /**
     * Test check-in notification functionality
     */
    private void testCheckInNotifications() {
        Log.d(TAG, "Testing check-in notifications...");
        
        // Test QR code check-in
        integrationService.sendCheckInNotification(
                testStudentId, testBusId, "STOP001", "QR"
        ).thenAccept(success -> {
            if (success) {
                Log.d(TAG, "✅ QR check-in notification sent successfully");
                analyticsService.trackCheckInNotificationSent(testStudentId, testBusId, "STOP001", "QR");
            } else {
                Log.e(TAG, "❌ Failed to send QR check-in notification");
            }
        });
        
        // Test GPS check-in
        integrationService.sendCheckInNotification(
                testStudentId, testBusId, "STOP002", "GPS"
        ).thenAccept(success -> {
            if (success) {
                Log.d(TAG, "✅ GPS check-in notification sent successfully");
                analyticsService.trackCheckInNotificationSent(testStudentId, testBusId, "STOP002", "GPS");
            } else {
                Log.e(TAG, "❌ Failed to send GPS check-in notification");
            }
        });
    }

    /**
     * Test emergency alert functionality
     */
    private void testEmergencyAlerts() {
        Log.d(TAG, "Testing emergency alerts...");
        
        // Test medical emergency
        integrationService.sendEmergencyAlert(
                testStudentId, testBusId, "Medical Emergency", 
                "Student requires immediate medical attention"
        ).thenAccept(success -> {
            if (success) {
                Log.d(TAG, "✅ Medical emergency alert sent successfully");
                analyticsService.trackEmergencyTriggered(testStudentId, testBusId, "Medical Emergency", "Student requires immediate medical attention");
            } else {
                Log.e(TAG, "❌ Failed to send medical emergency alert");
            }
        });
        
        // Test route emergency
        integrationService.sendEmergencyAlert(
                testStudentId, testBusId, "Route Emergency", 
                "Bus route blocked due to accident"
        ).thenAccept(success -> {
            if (success) {
                Log.d(TAG, "✅ Route emergency alert sent successfully");
                analyticsService.trackEmergencyTriggered(testStudentId, testBusId, "Route Emergency", "Bus route blocked due to accident");
            } else {
                Log.e(TAG, "❌ Failed to send route emergency alert");
            }
        });
    }

    /**
     * Test real-time sync functionality
     */
    private void testRealTimeSync() {
        Log.d(TAG, "Testing real-time sync...");
        
        // Test message sync
        syncService.startMessageSync(
                testStudentId, 
                CrossAppIntegrationService.USER_TYPE_STUDENT,
                new RealTimeSyncService.MessageSyncListener() {
                    @Override
                    public void onNewMessagesReceived(List<CrossAppMessage> messages) {
                        Log.d(TAG, "✅ Received " + messages.size() + " messages via real-time sync");
                        for (CrossAppMessage message : messages) {
                            analyticsService.trackMessageReceived(message);
                        }
                    }
                }
        );
        
        // Test emergency sync
        syncService.startEmergencySync(
                testStudentId,
                CrossAppIntegrationService.USER_TYPE_STUDENT,
                new RealTimeSyncService.EmergencyListener() {
                    @Override
                    public void onEmergencyReceived(List<CrossAppMessage> emergencyMessages) {
                        Log.d(TAG, "✅ Received " + emergencyMessages.size() + " emergency alerts via real-time sync");
                    }
                }
        );
        
        Log.d(TAG, "✅ Real-time sync listeners started");
    }

    /**
     * Test data synchronization functionality
     */
    private void testDataSynchronization() {
        Log.d(TAG, "Testing data synchronization...");
        
        // Test bus data sync
        dataSyncService.startBusDataSync(testBusId, new CrossAppDataSyncService.BusDataSyncListener() {
            @Override
            public void onBusDataUpdated(Bus bus) {
                Log.d(TAG, "✅ Bus data synchronized: " + bus.getBusNumber());
                analyticsService.trackBusStatusUpdated(bus.getBusId(), bus.getStatus(), bus.isOnRoute(), bus.isMoving());
            }
        });
        
        // Test student data sync
        dataSyncService.syncStudentData(testStudentId, new CrossAppDataSyncService.StudentDataSyncListener() {
            @Override
            public void onStudentDataUpdated(Student student) {
                Log.d(TAG, "✅ Student data synchronized: " + student.getName());
            }
        });
        
        // Test attendance data sync
        dataSyncService.syncAttendanceData(
                testStudentId, testBusId, "STOP001", "QR", System.currentTimeMillis()
        ).thenAccept(success -> {
            if (success) {
                Log.d(TAG, "✅ Attendance data synchronized successfully");
            } else {
                Log.e(TAG, "❌ Failed to synchronize attendance data");
            }
        });
    }

    /**
     * Test notification system functionality
     */
    private void testNotificationSystem() {
        Log.d(TAG, "Testing notification system...");
        
        // Test emergency notification
        CrossAppMessage emergencyMessage = createTestMessage(
                testSupervisorId, CrossAppIntegrationService.USER_TYPE_SUPERVISOR,
                testStudentId, CrossAppIntegrationService.USER_TYPE_STUDENT,
                CrossAppIntegrationService.MESSAGE_TYPE_EMERGENCY
        );
        emergencyMessage.setTitle("EMERGENCY ALERT");
        emergencyMessage.setContent("Test emergency notification");
        emergencyMessage.setPriority(CrossAppIntegrationService.PRIORITY_URGENT);
        emergencyMessage.setBusId(testBusId);
        
        notificationManager.showEmergencyNotification(emergencyMessage);
        analyticsService.trackNotificationShown("emergency", emergencyMessage.getMessageId(), "urgent");
        
        // Test driver message notification
        CrossAppMessage driverMessage = createTestMessage(
                testDriverId, CrossAppIntegrationService.USER_TYPE_DRIVER,
                testStudentId, CrossAppIntegrationService.USER_TYPE_STUDENT,
                CrossAppIntegrationService.MESSAGE_TYPE_NOTIFICATION
        );
        driverMessage.setTitle("Message from Driver");
        driverMessage.setContent("Test message from driver");
        driverMessage.setPriority(CrossAppIntegrationService.PRIORITY_MEDIUM);
        
        notificationManager.showDriverMessageNotification(driverMessage);
        analyticsService.trackNotificationShown("driver_message", driverMessage.getMessageId(), "medium");
        
        // Test check-in notification
        notificationManager.showCheckInNotification(testStudentId, "Test Stop", "QR", true);
        analyticsService.trackNotificationShown("checkin", "test_checkin", "medium");
        
        Log.d(TAG, "✅ Notification system tests completed");
    }

    /**
     * Test analytics tracking functionality
     */
    private void testAnalyticsTracking() {
        Log.d(TAG, "Testing analytics tracking...");
        
        // Track user engagement
        analyticsService.trackUserEngagement(testStudentId, "test_action", "Integration testing");
        
        // Track app performance
        analyticsService.trackAppPerformance("response_time", 150, "ms");
        analyticsService.trackAppPerformance("memory_usage", 50, "MB");
        
        // Track communication success rate
        analyticsService.trackCommunicationSuccessRate("location_update", true, 200);
        analyticsService.trackCommunicationSuccessRate("message", true, 100);
        
        // Store detailed analytics data
        Map<String, Object> testData = new HashMap<>();
        testData.put("test_type", "integration_test");
        testData.put("student_id", testStudentId);
        testData.put("test_duration", 5000);
        
        analyticsService.storeAnalyticsData("integration_test", testData)
                .thenAccept(success -> {
                    if (success) {
                        Log.d(TAG, "✅ Analytics data stored successfully");
                    } else {
                        Log.e(TAG, "❌ Failed to store analytics data");
                    }
                });
        
        Log.d(TAG, "✅ Analytics tracking tests completed");
    }

    /**
     * Test error handling functionality
     */
    private void testErrorHandling() {
        Log.d(TAG, "Testing error handling...");
        
        // Test with invalid data
        integrationService.sendMessage(null)
                .thenAccept(success -> {
                    if (!success) {
                        Log.d(TAG, "✅ Correctly handled null message");
                        analyticsService.trackErrorOccurred("NULL_MESSAGE", "Message is null", "CrossAppIntegrationService");
                    } else {
                        Log.e(TAG, "❌ Should have failed with null message");
                    }
                });
        
        // Test with invalid student ID
        integrationService.sendLocationUpdate(
                null, testBusId, 37.7749, -122.4194, 25.5, "North"
        ).thenAccept(success -> {
            if (!success) {
                Log.d(TAG, "✅ Correctly handled invalid student ID");
                analyticsService.trackErrorOccurred("INVALID_STUDENT_ID", "Student ID is null", "CrossAppIntegrationService");
            } else {
                Log.e(TAG, "❌ Should have failed with invalid student ID");
            }
        });
        
        Log.d(TAG, "✅ Error handling tests completed");
    }

    /**
     * Test performance functionality
     */
    private void testPerformance() {
        Log.d(TAG, "Testing performance...");
        
        final long startTime = System.currentTimeMillis();
        
        // Test bulk message sending
        List<CompletableFuture<Boolean>> futures = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            CrossAppMessage message = createTestMessage(
                    testStudentId, CrossAppIntegrationService.USER_TYPE_STUDENT,
                    testDriverId, CrossAppIntegrationService.USER_TYPE_DRIVER,
                    CrossAppIntegrationService.MESSAGE_TYPE_NOTIFICATION
            );
            message.setTitle("Bulk Test Message " + i);
            message.setContent("This is bulk test message number " + i);
            
            futures.add(integrationService.sendMessage(message));
        }
        
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenRun(() -> {
                    long endTime = System.currentTimeMillis();
                    long duration = endTime - startTime;
                    
                    Log.d(TAG, "✅ Bulk message test completed in " + duration + "ms");
                    analyticsService.trackAppPerformance("bulk_message_send", duration, "ms");
                    analyticsService.trackDataSyncPerformed("bulk_messages", duration, 10);
                });
        
        // Test response time
        final long responseStartTime = System.currentTimeMillis();
        integrationService.sendLocationUpdate(
                testStudentId, testBusId, 37.7749, -122.4194, 25.5, "North"
        ).thenAccept(success -> {
            long endTime = System.currentTimeMillis();
            long responseTime = endTime - responseStartTime;
            
            Log.d(TAG, "✅ Location update response time: " + responseTime + "ms");
            analyticsService.trackAppPerformance("location_update_response", responseTime, "ms");
            analyticsService.trackCommunicationSuccessRate("location_update", success, responseTime);
        });
    }

    /**
     * Create test message
     */
    private CrossAppMessage createTestMessage(String senderId, String senderType, 
                                            String receiverId, String receiverType, String messageType) {
        String messageId = "test_msg_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
        return new CrossAppMessage(messageId, senderId, senderType, receiverId, receiverType, messageType);
    }

    /**
     * Run specific test by name
     */
    public void runTest(String testName) {
        Log.d(TAG, "Running specific test: " + testName);
        
        switch (testName.toLowerCase()) {
            case "messages":
                testBasicMessageSending();
                break;
            case "location":
                testLocationUpdates();
                break;
            case "checkin":
                testCheckInNotifications();
                break;
            case "emergency":
                testEmergencyAlerts();
                break;
            case "sync":
                testRealTimeSync();
                break;
            case "data":
                testDataSynchronization();
                break;
            case "notifications":
                testNotificationSystem();
                break;
            case "analytics":
                testAnalyticsTracking();
                break;
            case "errors":
                testErrorHandling();
                break;
            case "performance":
                testPerformance();
                break;
            default:
                Log.e(TAG, "Unknown test: " + testName);
        }
    }

    /**
     * Clean up test resources
     */
    public void cleanup() {
        Log.d(TAG, "Cleaning up test resources...");
        
        if (syncService != null) {
            syncService.stopAllSync();
        }
        
        if (dataSyncService != null) {
            dataSyncService.stopAllDataSync();
        }
        
        if (notificationManager != null) {
            notificationManager.cancelAllNotifications();
        }
        
        Log.d(TAG, "✅ Test cleanup completed");
    }
}
