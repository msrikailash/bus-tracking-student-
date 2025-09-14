package com.example.student.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.student.models.Student;
import com.example.student.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.CompletableFuture;

public class AuthService {
    private static final String TAG = "AuthService";
    private static AuthService instance;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private SharedPreferences preferences;
    private Student currentStudent;
    private CrossAppIntegrationService crossAppService;
    private RealTimeSyncService syncService;

    private AuthService(Context context) {
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        preferences = context.getSharedPreferences("student_bus_prefs", Context.MODE_PRIVATE);
        
        // Initialize cross-app services
        crossAppService = CrossAppIntegrationService.getInstance(context);
        syncService = RealTimeSyncService.getInstance(context);
        
        // Restore demo login state if available
        restoreDemoLoginState();
    }

    public static synchronized AuthService getInstance(Context context) {
        if (instance == null) {
            instance = new AuthService(context);
        }
        return instance;
    }

    public CompletableFuture<Boolean> login(String studentId, String password) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        Log.d(TAG, "Attempting login for studentId: " + studentId);
        
        // Demo mode - check against demo accounts first
        if (isDemoAccount(studentId, password)) {
            Log.d(TAG, "Demo login successful for: " + studentId);
            currentStudent = createDemoStudent(studentId);
            saveLoginState(studentId);
            future.complete(true);
            return future;
        }
        
        Log.d(TAG, "Not a demo account, trying Firebase authentication");
        
        // Try Firebase authentication for real accounts
        String email = studentId + "@student.school.edu";
        
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Firebase login successful");
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            loadStudentData(user.getUid(), studentId)
                                    .thenAccept(student -> {
                                        if (student != null) {
                                            currentStudent = student;
                                            saveLoginState(studentId);
                                            future.complete(true);
                                        } else {
                                            future.complete(false);
                                        }
                                    })
                                    .exceptionally(throwable -> {
                                        Log.e(TAG, "Error loading student data", throwable);
                                        future.complete(false);
                                        return null;
                                    });
                        } else {
                            future.complete(false);
                        }
                    } else {
                        Log.e(TAG, "Firebase login failed", task.getException());
                        future.complete(false);
                    }
                });
        
        return future;
    }

    // Demo authentication methods
    private boolean isDemoAccount(String studentId, String password) {
        Log.d(TAG, "Checking demo account: " + studentId + " with password length: " + password.length());
        
        // Handle null or empty inputs
        if (studentId == null || password == null || studentId.trim().isEmpty() || password.trim().isEmpty()) {
            Log.d(TAG, "Null or empty credentials");
            return false;
        }
        
        // Trim whitespace
        studentId = studentId.trim();
        password = password.trim();
        
        // Demo accounts for testing
        boolean isDemo = (studentId.equals("demo001") && password.equals("demo123")) ||
                        (studentId.equals("student001") && password.equals("password123")) ||
                        (studentId.equals("test") && password.equals("test123")) ||
                        (studentId.equals("admin") && password.equals("admin123")) ||
                        // Additional fallback demo accounts
                        (studentId.equals("demo") && password.equals("demo")) ||
                        (studentId.equals("user") && password.equals("user"));
        
        Log.d(TAG, "Is demo account: " + isDemo);
        return isDemo;
    }

    private Student createDemoStudent(String studentId) {
        Student student = new Student();
        student.setStudentId(studentId);
        
        switch (studentId) {
            case "demo001":
                student.setName("Demo Student");
                student.setEmail("demo001@student.school.edu");
                student.setGrade("10th Grade");
                student.setAssignedBusId("Bus-42");
                student.setParentPhone("+1-555-0456");
                student.setEmergencyContact("+1-555-0123");
                break;
            case "student001":
                student.setName("Alex Johnson");
                student.setEmail("student001@student.school.edu");
                student.setGrade("11th Grade");
                student.setAssignedBusId("Bus-15");
                student.setParentPhone("+1-555-0321");
                student.setEmergencyContact("+1-555-0789");
                break;
            case "test":
                student.setName("Test User");
                student.setEmail("test@student.school.edu");
                student.setGrade("9th Grade");
                student.setAssignedBusId("Bus-08");
                student.setParentPhone("+1-555-0987");
                student.setEmergencyContact("+1-555-0654");
                break;
            case "admin":
                student.setName("Admin User");
                student.setEmail("admin@student.school.edu");
                student.setGrade("12th Grade");
                student.setAssignedBusId("Bus-01");
                student.setParentPhone("+1-555-0222");
                student.setEmergencyContact("+1-555-0111");
                break;
            case "demo":
                student.setName("Simple Demo");
                student.setEmail("demo@student.school.edu");
                student.setGrade("10th Grade");
                student.setAssignedBusId("Bus-99");
                student.setParentPhone("+1-555-0001");
                student.setEmergencyContact("+1-555-0000");
                break;
            case "user":
                student.setName("Test User");
                student.setEmail("user@student.school.edu");
                student.setGrade("11th Grade");
                student.setAssignedBusId("Bus-50");
                student.setParentPhone("+1-555-0002");
                student.setEmergencyContact("+1-555-0003");
                break;
            default:
                student.setName("Demo User");
                student.setEmail(studentId + "@student.school.edu");
                student.setGrade("10th Grade");
                student.setAssignedBusId("Bus-99");
                student.setParentPhone("+1-555-0001");
                student.setEmergencyContact("+1-555-0000");
                break;
        }
        
        return student;
    }

    public CompletableFuture<Boolean> register(String studentId, String name, String email, String password, String grade) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            Student student = new Student(studentId, name, email, grade, "");
                            saveStudentToFirestore(user.getUid(), student)
                                    .thenAccept(success -> {
                                        if (success) {
                                            currentStudent = student;
                                            saveLoginState(studentId);
                                            future.complete(true);
                                        } else {
                                            future.complete(false);
                                        }
                                    })
                                    .exceptionally(throwable -> {
                                        Log.e(TAG, "Error saving student data", throwable);
                                        future.complete(false);
                                        return null;
                                    });
                        } else {
                            future.complete(false);
                        }
                    } else {
                        Log.e(TAG, "Registration failed", task.getException());
                        future.complete(false);
                    }
                });
        
        return future;
    }

    public CompletableFuture<Boolean> resetPassword(String email) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        future.complete(true);
                    } else {
                        Log.e(TAG, "Password reset failed", task.getException());
                        future.complete(false);
                    }
                });
        
        return future;
    }

    public CompletableFuture<Student> loadStudentData(String uid, String studentId) {
        CompletableFuture<Student> future = new CompletableFuture<>();
        
        firestore.collection(Constants.COLLECTION_STUDENTS)
                .document(uid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Student student = document.toObject(Student.class);
                            future.complete(student);
                        } else {
                            future.complete(null);
                        }
                    } else {
                        Log.e(TAG, "Error loading student data", task.getException());
                        future.complete(null);
                    }
                });
        
        return future;
    }

    public CompletableFuture<Boolean> saveStudentToFirestore(String uid, Student student) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        firestore.collection(Constants.COLLECTION_STUDENTS)
                .document(uid)
                .set(student)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        future.complete(true);
                    } else {
                        Log.e(TAG, "Error saving student data", task.getException());
                        future.complete(false);
                    }
                });
        
        return future;
    }

    public CompletableFuture<Boolean> updateStudentProfile(Student student) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            student.setUpdatedAt(new java.util.Date());
            firestore.collection(Constants.COLLECTION_STUDENTS)
                    .document(user.getUid())
                    .set(student)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            currentStudent = student;
                            future.complete(true);
                        } else {
                            Log.e(TAG, "Error updating student profile", task.getException());
                            future.complete(false);
                        }
                    });
        } else {
            future.complete(false);
        }
        
        return future;
    }

    public CompletableFuture<Boolean> changePassword(String currentPassword, String newPassword) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            user.updatePassword(newPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            future.complete(true);
                        } else {
                            Log.e(TAG, "Error changing password", task.getException());
                            future.complete(false);
                        }
                    });
        } else {
            future.complete(false);
        }
        
        return future;
    }

    public void logout() {
        firebaseAuth.signOut();
        
        // Stop cross-app integration
        if (syncService != null) {
            syncService.stopAllSync();
        }
        
        currentStudent = null;
        clearLoginState();
        Log.d(TAG, "User logged out successfully");
    }

    public boolean isLoggedIn() {
        // Check if user is logged in via Firebase OR demo mode
        boolean firebaseLoggedIn = firebaseAuth.getCurrentUser() != null && currentStudent != null;
        boolean demoLoggedIn = preferences.getBoolean(Constants.PREF_IS_LOGGED_IN, false) && currentStudent != null;
        
        return firebaseLoggedIn || demoLoggedIn;
    }

    public Student getCurrentStudent() {
        return currentStudent;
    }

    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    private void saveLoginState(String studentId) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(Constants.PREF_IS_LOGGED_IN, true);
        editor.putString(Constants.PREF_STUDENT_ID, studentId);
        editor.apply();
    }

    private void clearLoginState() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(Constants.PREF_IS_LOGGED_IN, false);
        editor.remove(Constants.PREF_STUDENT_ID);
        editor.apply();
    }
    
    private void restoreDemoLoginState() {
        boolean isLoggedIn = preferences.getBoolean(Constants.PREF_IS_LOGGED_IN, false);
        String studentId = preferences.getString(Constants.PREF_STUDENT_ID, null);
        
        if (isLoggedIn && studentId != null && isDemoAccount(studentId, "dummy")) {
            // Restore the demo student
            currentStudent = createDemoStudent(studentId);
            Log.d(TAG, "Restored demo login state for: " + studentId);
        }
    }

    public boolean wasLoggedIn() {
        return preferences.getBoolean(Constants.PREF_IS_LOGGED_IN, false);
    }

    public String getLastStudentId() {
        return preferences.getString(Constants.PREF_STUDENT_ID, "");
    }

    /**
     * Start cross-app integration services after successful login
     */
    public void startCrossAppIntegration() {
        if (currentStudent != null && syncService != null) {
            Log.d(TAG, "Starting cross-app integration for student: " + currentStudent.getStudentId());
            
            // Start listening for messages from drivers and supervisors
            syncService.startMessageSync(
                    currentStudent.getStudentId(), 
                    CrossAppIntegrationService.USER_TYPE_STUDENT,
                    new RealTimeSyncService.MessageSyncListener() {
                        @Override
                        public void onNewMessagesReceived(java.util.List<com.example.student.models.CrossAppMessage> messages) {
                            // Handle incoming messages
                            Log.d(TAG, "Received " + messages.size() + " new cross-app messages");
                            // You can add notification handling here
                        }
                    }
            );
            
            // Start listening for emergency alerts
            syncService.startEmergencySync(
                    currentStudent.getStudentId(),
                    CrossAppIntegrationService.USER_TYPE_STUDENT,
                    new RealTimeSyncService.EmergencyListener() {
                        @Override
                        public void onEmergencyReceived(java.util.List<com.example.student.models.CrossAppMessage> emergencyMessages) {
                            // Handle emergency alerts
                            Log.d(TAG, "Received " + emergencyMessages.size() + " emergency alerts");
                            // You can add emergency notification handling here
                        }
                    }
            );
            
            // Start student status sync
            syncService.startStudentStatusSync(
                    currentStudent.getStudentId(),
                    new RealTimeSyncService.StudentStatusListener() {
                        @Override
                        public void onStudentStatusUpdated(Student student) {
                            // Handle student status updates
                            Log.d(TAG, "Student status updated: " + student.getName());
                            currentStudent = student; // Update current student
                        }
                    }
            );
        }
    }

    /**
     * Get cross-app integration service
     */
    public CrossAppIntegrationService getCrossAppService() {
        return crossAppService;
    }

    /**
     * Get real-time sync service
     */
    public RealTimeSyncService getSyncService() {
        return syncService;
    }
}

