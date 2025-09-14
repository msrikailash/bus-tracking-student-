package com.example.student.services;

import android.content.Context;
import android.util.Log;

import com.example.student.models.Bus;
import com.example.student.models.Student;
import com.example.student.models.CrossAppMessage;
import com.example.student.utils.Constants;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class RealTimeSyncService {
    private static final String TAG = "RealTimeSyncService";
    private static RealTimeSyncService instance;
    
    private FirebaseFirestore firestore;
    private Context context;
    private List<ListenerRegistration> listeners;
    private CrossAppIntegrationService integrationService;
    
    // Sync intervals
    private static final int BUS_LOCATION_SYNC_INTERVAL = 10000; // 10 seconds
    private static final int STUDENT_STATUS_SYNC_INTERVAL = 30000; // 30 seconds
    private static final int MESSAGE_SYNC_INTERVAL = 5000; // 5 seconds

    private RealTimeSyncService(Context context) {
        this.context = context.getApplicationContext();
        this.firestore = FirebaseFirestore.getInstance();
        this.listeners = new ArrayList<>();
        this.integrationService = CrossAppIntegrationService.getInstance(context);
    }

    public static synchronized RealTimeSyncService getInstance(Context context) {
        if (instance == null) {
            instance = new RealTimeSyncService(context);
        }
        return instance;
    }

    /**
     * Start real-time sync for a student's assigned bus
     */
    public void startBusLocationSync(String studentId, String busId, BusLocationListener listener) {
        Log.d(TAG, "Starting bus location sync for student: " + studentId + ", bus: " + busId);
        
        ListenerRegistration registration = firestore.collection(Constants.COLLECTION_BUSES)
                .document(busId)
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Log.e(TAG, "Error listening to bus location updates", e);
                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        Bus bus = snapshot.toObject(Bus.class);
                        if (bus != null) {
                            Log.d(TAG, "Bus location updated: " + bus.getCurrentLocation());
                            listener.onBusLocationUpdated(bus);
                            
                            // Send location update to other apps
                            if (bus.getCurrentLocation() != null) {
                                integrationService.sendLocationUpdate(
                                        studentId, busId,
                                        bus.getCurrentLocation().latitude,
                                        bus.getCurrentLocation().longitude,
                                        bus.getSpeed(),
                                        bus.getDirection()
                                );
                            }
                        }
                    }
                });

        listeners.add(registration);
    }

    /**
     * Start listening for cross-app messages
     */
    public void startMessageSync(String userId, String userType, MessageSyncListener listener) {
        Log.d(TAG, "Starting message sync for user: " + userId + ", type: " + userType);
        
        Query query = firestore.collection(Constants.COLLECTION_CROSS_APP_MESSAGES)
                .whereEqualTo("receiverId", userId)
                .whereEqualTo("receiverType", userType)
                .whereEqualTo("isRead", false)
                .orderBy("timestamp", Query.Direction.DESCENDING);

        ListenerRegistration registration = query.addSnapshotListener((snapshots, e) -> {
            if (e != null) {
                Log.e(TAG, "Error listening for messages", e);
                return;
            }

            if (snapshots != null && !snapshots.isEmpty()) {
                List<CrossAppMessage> newMessages = new ArrayList<>();
                for (DocumentSnapshot doc : snapshots.getDocuments()) {
                    CrossAppMessage message = doc.toObject(CrossAppMessage.class);
                    if (message != null) {
                        message.setMessageId(doc.getId());
                        newMessages.add(message);
                    }
                }
                
                if (!newMessages.isEmpty()) {
                    Log.d(TAG, "Received " + newMessages.size() + " new messages");
                    listener.onNewMessagesReceived(newMessages);
                }
            }
        });

        listeners.add(registration);
    }

    /**
     * Start listening for student status updates
     */
    public void startStudentStatusSync(String studentId, StudentStatusListener listener) {
        Log.d(TAG, "Starting student status sync for: " + studentId);
        
        ListenerRegistration registration = firestore.collection(Constants.COLLECTION_STUDENTS)
                .document(studentId)
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Log.e(TAG, "Error listening to student status updates", e);
                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        Student student = snapshot.toObject(Student.class);
                        if (student != null) {
                            Log.d(TAG, "Student status updated: " + student.getName());
                            listener.onStudentStatusUpdated(student);
                        }
                    }
                });

        listeners.add(registration);
    }

    /**
     * Start listening for route updates
     */
    public void startRouteSync(String routeId, RouteUpdateListener listener) {
        Log.d(TAG, "Starting route sync for: " + routeId);
        
        ListenerRegistration registration = firestore.collection(Constants.COLLECTION_ROUTES)
                .document(routeId)
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Log.e(TAG, "Error listening to route updates", e);
                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        // Route object would be created here if needed
                        Log.d(TAG, "Route updated: " + routeId);
                        listener.onRouteUpdated(routeId);
                    }
                });

        listeners.add(registration);
    }

    /**
     * Start listening for emergency alerts
     */
    public void startEmergencySync(String userId, String userType, EmergencyListener listener) {
        Log.d(TAG, "Starting emergency sync for user: " + userId);
        
        Query query = firestore.collection(Constants.COLLECTION_CROSS_APP_MESSAGES)
                .whereEqualTo("receiverId", userId)
                .whereEqualTo("receiverType", userType)
                .whereEqualTo("messageType", CrossAppIntegrationService.MESSAGE_TYPE_EMERGENCY)
                .whereEqualTo("priority", CrossAppIntegrationService.PRIORITY_URGENT)
                .orderBy("timestamp", Query.Direction.DESCENDING);

        ListenerRegistration registration = query.addSnapshotListener((snapshots, e) -> {
            if (e != null) {
                Log.e(TAG, "Error listening for emergency alerts", e);
                return;
            }

            if (snapshots != null && !snapshots.isEmpty()) {
                List<CrossAppMessage> emergencyMessages = new ArrayList<>();
                for (DocumentSnapshot doc : snapshots.getDocuments()) {
                    CrossAppMessage message = doc.toObject(CrossAppMessage.class);
                    if (message != null) {
                        message.setMessageId(doc.getId());
                        emergencyMessages.add(message);
                    }
                }
                
                if (!emergencyMessages.isEmpty()) {
                    Log.d(TAG, "Received " + emergencyMessages.size() + " emergency alerts");
                    listener.onEmergencyReceived(emergencyMessages);
                }
            }
        });

        listeners.add(registration);
    }

    /**
     * Stop all sync listeners
     */
    public void stopAllSync() {
        Log.d(TAG, "Stopping all sync listeners");
        for (ListenerRegistration listener : listeners) {
            listener.remove();
        }
        listeners.clear();
    }

    /**
     * Stop specific sync by type
     */
    public void stopSync(String syncType) {
        // Implementation to stop specific sync types
        Log.d(TAG, "Stopping sync: " + syncType);
    }

    // Listener interfaces
    public interface BusLocationListener {
        void onBusLocationUpdated(Bus bus);
    }

    public interface MessageSyncListener {
        void onNewMessagesReceived(List<CrossAppMessage> messages);
    }

    public interface StudentStatusListener {
        void onStudentStatusUpdated(Student student);
    }

    public interface RouteUpdateListener {
        void onRouteUpdated(String routeId);
    }

    public interface EmergencyListener {
        void onEmergencyReceived(List<CrossAppMessage> emergencyMessages);
    }
}
