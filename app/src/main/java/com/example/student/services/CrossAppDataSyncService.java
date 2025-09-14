package com.example.student.services;

import android.content.Context;
import android.util.Log;

import com.example.student.models.Bus;
import com.example.student.models.Student;
import com.example.student.models.Driver;
import com.example.student.models.Supervisor;
import com.example.student.models.Route;
import com.example.student.models.Schedule;
import com.example.student.utils.Constants;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Service for syncing data between student, driver, and supervisor apps
 * Handles offline data caching and conflict resolution
 */
public class CrossAppDataSyncService {
    private static final String TAG = "CrossAppDataSync";
    private static CrossAppDataSyncService instance;
    
    private FirebaseFirestore firestore;
    private Context context;
    private List<ListenerRegistration> listeners;
    
    // Sync intervals
    private static final int BUS_DATA_SYNC_INTERVAL = 30000; // 30 seconds
    private static final int ROUTE_DATA_SYNC_INTERVAL = 60000; // 1 minute
    private static final int SCHEDULE_DATA_SYNC_INTERVAL = 300000; // 5 minutes

    private CrossAppDataSyncService(Context context) {
        this.context = context.getApplicationContext();
        this.firestore = FirebaseFirestore.getInstance();
        this.listeners = new ArrayList<>();
    }

    public static synchronized CrossAppDataSyncService getInstance(Context context) {
        if (instance == null) {
            instance = new CrossAppDataSyncService(context);
        }
        return instance;
    }

    /**
     * Start syncing bus data with driver and supervisor apps
     */
    public void startBusDataSync(String busId, BusDataSyncListener listener) {
        Log.d(TAG, "Starting bus data sync for bus: " + busId);
        
        ListenerRegistration registration = firestore.collection(Constants.COLLECTION_BUSES)
                .document(busId)
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Log.e(TAG, "Error syncing bus data", e);
                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        Bus bus = snapshot.toObject(Bus.class);
                        if (bus != null) {
                            Log.d(TAG, "Bus data synced: " + bus.getBusNumber());
                            listener.onBusDataUpdated(bus);
                        }
                    }
                });

        listeners.add(registration);
    }

    /**
     * Start syncing route data
     */
    public void startRouteDataSync(String routeId, RouteDataSyncListener listener) {
        Log.d(TAG, "Starting route data sync for route: " + routeId);
        
        ListenerRegistration registration = firestore.collection(Constants.COLLECTION_ROUTES)
                .document(routeId)
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Log.e(TAG, "Error syncing route data", e);
                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        Route route = snapshot.toObject(Route.class);
                        if (route != null) {
                            Log.d(TAG, "Route data synced: " + route.getRouteName());
                            listener.onRouteDataUpdated(route);
                        }
                    }
                });

        listeners.add(registration);
    }

    /**
     * Start syncing schedule data
     */
    public void startScheduleDataSync(String routeId, ScheduleDataSyncListener listener) {
        Log.d(TAG, "Starting schedule data sync for route: " + routeId);
        
        Query query = firestore.collection(Constants.COLLECTION_SCHEDULES)
                .whereEqualTo("routeId", routeId)
                .whereEqualTo("isActive", true);

        ListenerRegistration registration = query.addSnapshotListener((snapshots, e) -> {
            if (e != null) {
                Log.e(TAG, "Error syncing schedule data", e);
                return;
            }

            if (snapshots != null && !snapshots.isEmpty()) {
                List<Schedule> schedules = new ArrayList<>();
                for (DocumentSnapshot doc : snapshots.getDocuments()) {
                    Schedule schedule = doc.toObject(Schedule.class);
                    if (schedule != null) {
                        schedules.add(schedule);
                    }
                }
                Log.d(TAG, "Schedule data synced: " + schedules.size() + " schedules");
                listener.onScheduleDataUpdated(schedules);
            }
        });

        listeners.add(registration);
    }

    /**
     * Sync student data with supervisor app
     */
    public void syncStudentData(String studentId, StudentDataSyncListener listener) {
        Log.d(TAG, "Syncing student data for: " + studentId);
        
        ListenerRegistration registration = firestore.collection(Constants.COLLECTION_STUDENTS)
                .document(studentId)
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Log.e(TAG, "Error syncing student data", e);
                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        Student student = snapshot.toObject(Student.class);
                        if (student != null) {
                            Log.d(TAG, "Student data synced: " + student.getName());
                            listener.onStudentDataUpdated(student);
                        }
                    }
                });

        listeners.add(registration);
    }

    /**
     * Get driver information by bus ID
     */
    public CompletableFuture<Driver> getDriverByBusId(String busId) {
        CompletableFuture<Driver> future = new CompletableFuture<>();
        
        firestore.collection(Constants.COLLECTION_BUSES)
                .document(busId)
                .get()
                .addOnSuccessListener(busDoc -> {
                    if (busDoc.exists()) {
                        Bus bus = busDoc.toObject(Bus.class);
                        if (bus != null && bus.getDriverName() != null) {
                            // Get driver details
                            firestore.collection(Constants.COLLECTION_DRIVERS)
                                    .whereEqualTo("name", bus.getDriverName())
                                    .limit(1)
                                    .get()
                                    .addOnSuccessListener(driverDocs -> {
                                        if (!driverDocs.isEmpty()) {
                                            Driver driver = driverDocs.getDocuments().get(0).toObject(Driver.class);
                                            future.complete(driver);
                                        } else {
                                            future.complete(null);
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e(TAG, "Error getting driver by bus ID", e);
                                        future.complete(null);
                                    });
                        } else {
                            future.complete(null);
                        }
                    } else {
                        future.complete(null);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error getting bus for driver lookup", e);
                    future.complete(null);
                });
        
        return future;
    }

    /**
     * Get supervisor information by route ID
     */
    public CompletableFuture<List<Supervisor>> getSupervisorsByRouteId(String routeId) {
        CompletableFuture<List<Supervisor>> future = new CompletableFuture<>();
        
        firestore.collection(Constants.COLLECTION_SUPERVISORS)
                .whereArrayContains("assignedRoutes", routeId)
                .whereEqualTo("isActive", true)
                .get()
                .addOnSuccessListener(supervisorDocs -> {
                    List<Supervisor> supervisors = new ArrayList<>();
                    for (DocumentSnapshot doc : supervisorDocs.getDocuments()) {
                        Supervisor supervisor = doc.toObject(Supervisor.class);
                        if (supervisor != null) {
                            supervisors.add(supervisor);
                        }
                    }
                    Log.d(TAG, "Found " + supervisors.size() + " supervisors for route: " + routeId);
                    future.complete(supervisors);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error getting supervisors by route ID", e);
                    future.complete(new ArrayList<>());
                });
        
        return future;
    }

    /**
     * Sync attendance data with supervisor app
     */
    public CompletableFuture<Boolean> syncAttendanceData(String studentId, String busId, 
                                                        String stopId, String checkInType, 
                                                        long timestamp) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        try {
            // Create attendance record
            java.util.Map<String, Object> attendanceData = new java.util.HashMap<>();
            attendanceData.put("studentId", studentId);
            attendanceData.put("busId", busId);
            attendanceData.put("stopId", stopId);
            attendanceData.put("checkInType", checkInType);
            attendanceData.put("timestamp", timestamp);
            attendanceData.put("syncedAt", System.currentTimeMillis());
            
            firestore.collection(Constants.COLLECTION_ATTENDANCE)
                    .add(attendanceData)
                    .addOnSuccessListener(documentReference -> {
                        Log.d(TAG, "Attendance data synced: " + documentReference.getId());
                        future.complete(true);
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error syncing attendance data", e);
                        future.complete(false);
                    });
        } catch (Exception e) {
            Log.e(TAG, "Error creating attendance data", e);
            future.complete(false);
        }
        
        return future;
    }

    /**
     * Get real-time bus status from driver app
     */
    public void startBusStatusSync(String busId, BusStatusSyncListener listener) {
        Log.d(TAG, "Starting bus status sync for bus: " + busId);
        
        ListenerRegistration registration = firestore.collection(Constants.COLLECTION_BUSES)
                .document(busId)
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        Log.e(TAG, "Error syncing bus status", e);
                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        Bus bus = snapshot.toObject(Bus.class);
                        if (bus != null) {
                            String status = bus.getStatus();
                            boolean isOnRoute = bus.isOnRoute();
                            boolean isMoving = bus.isMoving();
                            
                            Log.d(TAG, "Bus status synced - Status: " + status + 
                                  ", On Route: " + isOnRoute + ", Moving: " + isMoving);
                            
                            listener.onBusStatusUpdated(status, isOnRoute, isMoving, bus);
                        }
                    }
                });

        listeners.add(registration);
    }

    /**
     * Stop all data sync listeners
     */
    public void stopAllDataSync() {
        Log.d(TAG, "Stopping all data sync listeners");
        for (ListenerRegistration listener : listeners) {
            listener.remove();
        }
        listeners.clear();
    }

    // Listener interfaces
    public interface BusDataSyncListener {
        void onBusDataUpdated(Bus bus);
    }

    public interface RouteDataSyncListener {
        void onRouteDataUpdated(Route route);
    }

    public interface ScheduleDataSyncListener {
        void onScheduleDataUpdated(List<Schedule> schedules);
    }

    public interface StudentDataSyncListener {
        void onStudentDataUpdated(Student student);
    }

    public interface BusStatusSyncListener {
        void onBusStatusUpdated(String status, boolean isOnRoute, boolean isMoving, Bus bus);
    }
}
