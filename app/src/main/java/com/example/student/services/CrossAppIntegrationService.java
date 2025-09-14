package com.example.student.services;

import android.content.Context;
import android.util.Log;

import com.example.student.models.CrossAppMessage;
import com.example.student.models.Student;
import com.example.student.models.Bus;
import com.example.student.models.Driver;
import com.example.student.models.Supervisor;
import com.example.student.utils.Constants;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class CrossAppIntegrationService {
    private static final String TAG = "CrossAppIntegration";
    private static CrossAppIntegrationService instance;
    
    private FirebaseFirestore firestore;
    private Context context;
    private List<ListenerRegistration> listeners;
    
    // Message types
    public static final String MESSAGE_TYPE_LOCATION_UPDATE = "location_update";
    public static final String MESSAGE_TYPE_CHECKIN = "checkin";
    public static final String MESSAGE_TYPE_EMERGENCY = "emergency";
    public static final String MESSAGE_TYPE_NOTIFICATION = "notification";
    public static final String MESSAGE_TYPE_STATUS_UPDATE = "status_update";
    public static final String MESSAGE_TYPE_ATTENDANCE = "attendance";
    
    // User types
    public static final String USER_TYPE_STUDENT = "student";
    public static final String USER_TYPE_DRIVER = "driver";
    public static final String USER_TYPE_SUPERVISOR = "supervisor";
    
    // Priority levels
    public static final String PRIORITY_LOW = "low";
    public static final String PRIORITY_MEDIUM = "medium";
    public static final String PRIORITY_HIGH = "high";
    public static final String PRIORITY_URGENT = "urgent";

    private CrossAppIntegrationService(Context context) {
        this.context = context.getApplicationContext();
        this.firestore = FirebaseFirestore.getInstance();
        this.listeners = new ArrayList<>();
    }

    public static synchronized CrossAppIntegrationService getInstance(Context context) {
        if (instance == null) {
            instance = new CrossAppIntegrationService(context);
        }
        return instance;
    }

    /**
     * Send a message to another app/user
     */
    public CompletableFuture<Boolean> sendMessage(CrossAppMessage message) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        try {
            // Add message to cross-app messages collection
            firestore.collection(Constants.COLLECTION_CROSS_APP_MESSAGES)
                    .add(message)
                    .addOnSuccessListener(documentReference -> {
                        Log.d(TAG, "Message sent successfully: " + documentReference.getId());
                        future.complete(true);
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Failed to send message", e);
                        future.complete(false);
                    });
        } catch (Exception e) {
            Log.e(TAG, "Error sending message", e);
            future.complete(false);
        }
        
        return future;
    }

    /**
     * Send location update to supervisor and driver
     */
    public CompletableFuture<Boolean> sendLocationUpdate(String studentId, String busId, 
                                                        double latitude, double longitude, 
                                                        double speed, String direction) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        try {
            // Get bus information to find driver and route
            firestore.collection(Constants.COLLECTION_BUSES)
                    .document(busId)
                    .get()
                    .addOnSuccessListener(busDoc -> {
                        if (busDoc.exists()) {
                            Bus bus = busDoc.toObject(Bus.class);
                            if (bus != null) {
                                // Send to driver
                                if (bus.getDriverName() != null) {
                                    CrossAppMessage driverMessage = new CrossAppMessage(
                                            generateMessageId(), studentId, USER_TYPE_STUDENT,
                                            bus.getDriverName(), USER_TYPE_DRIVER, MESSAGE_TYPE_LOCATION_UPDATE
                                    );
                                    driverMessage.setTitle("Student Location Update");
                                    driverMessage.setContent("Student " + studentId + " location updated");
                                    driverMessage.setBusId(busId);
                                    driverMessage.setPriority(PRIORITY_MEDIUM);
                                    
                                    Map<String, Object> locationData = new HashMap<>();
                                    locationData.put("latitude", latitude);
                                    locationData.put("longitude", longitude);
                                    locationData.put("speed", speed);
                                    locationData.put("direction", direction);
                                    locationData.put("timestamp", new Date());
                                    driverMessage.setData(locationData);
                                    
                                    sendMessage(driverMessage);
                                }
                                
                                // Send to supervisor
                                sendLocationUpdateToSupervisor(studentId, busId, latitude, longitude, speed, direction);
                                
                                future.complete(true);
                            } else {
                                future.complete(false);
                            }
                        } else {
                            future.complete(false);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Failed to get bus information", e);
                        future.complete(false);
                    });
        } catch (Exception e) {
            Log.e(TAG, "Error sending location update", e);
            future.complete(false);
        }
        
        return future;
    }

    /**
     * Send check-in notification to supervisor and driver
     */
    public CompletableFuture<Boolean> sendCheckInNotification(String studentId, String busId, 
                                                             String stopId, String checkInType) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        try {
            // Get bus and route information
            firestore.collection(Constants.COLLECTION_BUSES)
                    .document(busId)
                    .get()
                    .addOnSuccessListener(busDoc -> {
                        if (busDoc.exists()) {
                            Bus bus = busDoc.toObject(Bus.class);
                            if (bus != null) {
                                // Send to driver
                                CrossAppMessage driverMessage = new CrossAppMessage(
                                        generateMessageId(), studentId, USER_TYPE_STUDENT,
                                        bus.getDriverName(), USER_TYPE_DRIVER, MESSAGE_TYPE_CHECKIN
                                );
                                driverMessage.setTitle("Student Check-in");
                                driverMessage.setContent("Student " + studentId + " checked in at stop " + stopId);
                                driverMessage.setBusId(busId);
                                driverMessage.setStudentId(studentId);
                                driverMessage.setPriority(PRIORITY_HIGH);
                                
                                Map<String, Object> checkInData = new HashMap<>();
                                checkInData.put("stopId", stopId);
                                checkInData.put("checkInType", checkInType);
                                checkInData.put("timestamp", new Date());
                                driverMessage.setData(checkInData);
                                
                                sendMessage(driverMessage);
                                
                                // Send to supervisor
                                sendCheckInToSupervisor(studentId, busId, stopId, checkInType);
                                
                                future.complete(true);
                            } else {
                                future.complete(false);
                            }
                        } else {
                            future.complete(false);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Failed to get bus information for check-in", e);
                        future.complete(false);
                    });
        } catch (Exception e) {
            Log.e(TAG, "Error sending check-in notification", e);
            future.complete(false);
        }
        
        return future;
    }

    /**
     * Send emergency alert to supervisor and driver
     */
    public CompletableFuture<Boolean> sendEmergencyAlert(String studentId, String busId, 
                                                        String emergencyType, String message) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        try {
            // Get bus information
            firestore.collection(Constants.COLLECTION_BUSES)
                    .document(busId)
                    .get()
                    .addOnSuccessListener(busDoc -> {
                        if (busDoc.exists()) {
                            Bus bus = busDoc.toObject(Bus.class);
                            if (bus != null) {
                                // Send to driver
                                CrossAppMessage driverMessage = new CrossAppMessage(
                                        generateMessageId(), studentId, USER_TYPE_STUDENT,
                                        bus.getDriverName(), USER_TYPE_DRIVER, MESSAGE_TYPE_EMERGENCY
                                );
                                driverMessage.setTitle("EMERGENCY ALERT");
                                driverMessage.setContent(message);
                                driverMessage.setBusId(busId);
                                driverMessage.setStudentId(studentId);
                                driverMessage.setPriority(PRIORITY_URGENT);
                                
                                Map<String, Object> emergencyData = new HashMap<>();
                                emergencyData.put("emergencyType", emergencyType);
                                emergencyData.put("timestamp", new Date());
                                driverMessage.setData(emergencyData);
                                
                                sendMessage(driverMessage);
                                
                                // Send to all supervisors
                                sendEmergencyToSupervisors(studentId, busId, emergencyType, message);
                                
                                future.complete(true);
                            } else {
                                future.complete(false);
                            }
                        } else {
                            future.complete(false);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Failed to get bus information for emergency", e);
                        future.complete(false);
                    });
        } catch (Exception e) {
            Log.e(TAG, "Error sending emergency alert", e);
            future.complete(false);
        }
        
        return future;
    }

    /**
     * Listen for messages from other apps
     */
    public void startListeningForMessages(String userId, String userType, MessageListener listener) {
        Query query = firestore.collection(Constants.COLLECTION_CROSS_APP_MESSAGES)
                .whereEqualTo("receiverId", userId)
                .whereEqualTo("receiverType", userType)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(50);

        ListenerRegistration registration = query.addSnapshotListener((snapshots, e) -> {
            if (e != null) {
                Log.e(TAG, "Error listening for messages", e);
                return;
            }

            if (snapshots != null && !snapshots.isEmpty()) {
                List<CrossAppMessage> messages = new ArrayList<>();
                for (DocumentSnapshot doc : snapshots.getDocuments()) {
                    CrossAppMessage message = doc.toObject(CrossAppMessage.class);
                    if (message != null) {
                        message.setMessageId(doc.getId());
                        messages.add(message);
                    }
                }
                listener.onMessagesReceived(messages);
            }
        });

        listeners.add(registration);
    }

    /**
     * Mark message as read
     */
    public CompletableFuture<Boolean> markMessageAsRead(String messageId) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        firestore.collection(Constants.COLLECTION_CROSS_APP_MESSAGES)
                .document(messageId)
                .update("isRead", true, "readAt", new Date())
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Message marked as read: " + messageId);
                    future.complete(true);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to mark message as read", e);
                    future.complete(false);
                });
        
        return future;
    }

    /**
     * Get driver information
     */
    public CompletableFuture<Driver> getDriverInfo(String driverId) {
        CompletableFuture<Driver> future = new CompletableFuture<>();
        
        firestore.collection(Constants.COLLECTION_DRIVERS)
                .document(driverId)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        Driver driver = doc.toObject(Driver.class);
                        future.complete(driver);
                    } else {
                        future.complete(null);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to get driver info", e);
                    future.complete(null);
                });
        
        return future;
    }

    /**
     * Get supervisor information
     */
    public CompletableFuture<Supervisor> getSupervisorInfo(String supervisorId) {
        CompletableFuture<Supervisor> future = new CompletableFuture<>();
        
        firestore.collection(Constants.COLLECTION_SUPERVISORS)
                .document(supervisorId)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        Supervisor supervisor = doc.toObject(Supervisor.class);
                        future.complete(supervisor);
                    } else {
                        future.complete(null);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to get supervisor info", e);
                    future.complete(null);
                });
        
        return future;
    }

    /**
     * Stop all listeners
     */
    public void stopListening() {
        for (ListenerRegistration listener : listeners) {
            listener.remove();
        }
        listeners.clear();
    }

    // Private helper methods
    private void sendLocationUpdateToSupervisor(String studentId, String busId, 
                                               double latitude, double longitude, 
                                               double speed, String direction) {
        // Get all supervisors for the route
        firestore.collection(Constants.COLLECTION_BUSES)
                .document(busId)
                .get()
                .addOnSuccessListener(busDoc -> {
                    if (busDoc.exists()) {
                        Bus bus = busDoc.toObject(Bus.class);
                        if (bus != null && bus.getRouteId() != null) {
                            // Find supervisors assigned to this route
                            firestore.collection(Constants.COLLECTION_SUPERVISORS)
                                    .whereArrayContains("assignedRoutes", bus.getRouteId())
                                    .get()
                                    .addOnSuccessListener(supervisorDocs -> {
                                        for (DocumentSnapshot doc : supervisorDocs.getDocuments()) {
                                            Supervisor supervisor = doc.toObject(Supervisor.class);
                                            if (supervisor != null) {
                                                CrossAppMessage supervisorMessage = new CrossAppMessage(
                                                        generateMessageId(), studentId, USER_TYPE_STUDENT,
                                                        supervisor.getSupervisorId(), USER_TYPE_SUPERVISOR, 
                                                        MESSAGE_TYPE_LOCATION_UPDATE
                                                );
                                                supervisorMessage.setTitle("Student Location Update");
                                                supervisorMessage.setContent("Student " + studentId + " location updated on route " + bus.getRouteId());
                                                supervisorMessage.setBusId(busId);
                                                supervisorMessage.setRouteId(bus.getRouteId());
                                                supervisorMessage.setPriority(PRIORITY_MEDIUM);
                                                
                                                Map<String, Object> locationData = new HashMap<>();
                                                locationData.put("latitude", latitude);
                                                locationData.put("longitude", longitude);
                                                locationData.put("speed", speed);
                                                locationData.put("direction", direction);
                                                locationData.put("timestamp", new Date());
                                                supervisorMessage.setData(locationData);
                                                
                                                sendMessage(supervisorMessage);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void sendCheckInToSupervisor(String studentId, String busId, String stopId, String checkInType) {
        // Similar implementation to sendLocationUpdateToSupervisor
        firestore.collection(Constants.COLLECTION_BUSES)
                .document(busId)
                .get()
                .addOnSuccessListener(busDoc -> {
                    if (busDoc.exists()) {
                        Bus bus = busDoc.toObject(Bus.class);
                        if (bus != null && bus.getRouteId() != null) {
                            firestore.collection(Constants.COLLECTION_SUPERVISORS)
                                    .whereArrayContains("assignedRoutes", bus.getRouteId())
                                    .get()
                                    .addOnSuccessListener(supervisorDocs -> {
                                        for (DocumentSnapshot doc : supervisorDocs.getDocuments()) {
                                            Supervisor supervisor = doc.toObject(Supervisor.class);
                                            if (supervisor != null) {
                                                CrossAppMessage supervisorMessage = new CrossAppMessage(
                                                        generateMessageId(), studentId, USER_TYPE_STUDENT,
                                                        supervisor.getSupervisorId(), USER_TYPE_SUPERVISOR, 
                                                        MESSAGE_TYPE_CHECKIN
                                                );
                                                supervisorMessage.setTitle("Student Check-in");
                                                supervisorMessage.setContent("Student " + studentId + " checked in at stop " + stopId);
                                                supervisorMessage.setBusId(busId);
                                                supervisorMessage.setRouteId(bus.getRouteId());
                                                supervisorMessage.setStudentId(studentId);
                                                supervisorMessage.setPriority(PRIORITY_HIGH);
                                                
                                                Map<String, Object> checkInData = new HashMap<>();
                                                checkInData.put("stopId", stopId);
                                                checkInData.put("checkInType", checkInType);
                                                checkInData.put("timestamp", new Date());
                                                supervisorMessage.setData(checkInData);
                                                
                                                sendMessage(supervisorMessage);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void sendEmergencyToSupervisors(String studentId, String busId, String emergencyType, String message) {
        // Send to all active supervisors
        firestore.collection(Constants.COLLECTION_SUPERVISORS)
                .whereEqualTo("isActive", true)
                .get()
                .addOnSuccessListener(supervisorDocs -> {
                    for (DocumentSnapshot doc : supervisorDocs.getDocuments()) {
                        Supervisor supervisor = doc.toObject(Supervisor.class);
                        if (supervisor != null) {
                            CrossAppMessage supervisorMessage = new CrossAppMessage(
                                    generateMessageId(), studentId, USER_TYPE_STUDENT,
                                    supervisor.getSupervisorId(), USER_TYPE_SUPERVISOR, 
                                    MESSAGE_TYPE_EMERGENCY
                            );
                            supervisorMessage.setTitle("EMERGENCY ALERT");
                            supervisorMessage.setContent(message);
                            supervisorMessage.setBusId(busId);
                            supervisorMessage.setStudentId(studentId);
                            supervisorMessage.setPriority(PRIORITY_URGENT);
                            
                            Map<String, Object> emergencyData = new HashMap<>();
                            emergencyData.put("emergencyType", emergencyType);
                            emergencyData.put("timestamp", new Date());
                            supervisorMessage.setData(emergencyData);
                            
                            sendMessage(supervisorMessage);
                        }
                    }
                });
    }

    private String generateMessageId() {
        return "msg_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }

    // Interface for message listener
    public interface MessageListener {
        void onMessagesReceived(List<CrossAppMessage> messages);
    }
}
