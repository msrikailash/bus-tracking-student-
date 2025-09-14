package com.example.student.utils;

public class Constants {
    // Firebase Collections
    public static final String COLLECTION_STUDENTS = "students";
    public static final String COLLECTION_BUSES = "buses";
    public static final String COLLECTION_ROUTES = "routes";
    public static final String COLLECTION_STOPS = "stops";
    public static final String COLLECTION_TRIPS = "trips";
    public static final String COLLECTION_NOTIFICATIONS = "notifications";
    public static final String COLLECTION_SCHEDULES = "schedules";
    public static final String COLLECTION_ATTENDANCE = "attendance";
    public static final String COLLECTION_DRIVERS = "drivers";
    public static final String COLLECTION_SUPERVISORS = "supervisors";
    public static final String COLLECTION_CROSS_APP_MESSAGES = "cross_app_messages";

    // SharedPreferences Keys
    public static final String PREF_USER_ID = "user_id";
    public static final String PREF_STUDENT_ID = "student_id";
    public static final String PREF_IS_LOGGED_IN = "is_logged_in";
    public static final String PREF_BIOMETRIC_ENABLED = "biometric_enabled";
    public static final String PREF_NOTIFICATIONS_ENABLED = "notifications_enabled";
    public static final String PREF_LOCATION_ENABLED = "location_enabled";
    public static final String PREF_THEME = "theme";
    public static final String PREF_LANGUAGE = "language";

    // Location Constants
    public static final int LOCATION_UPDATE_INTERVAL = 10000; // 10 seconds
    public static final int LOCATION_FASTEST_INTERVAL = 5000; // 5 seconds
    public static final float LOCATION_ACCURACY_THRESHOLD = 50.0f; // 50 meters
    public static final int CHECKIN_RADIUS = 100; // 100 meters

    // Notification Constants
    public static final int NOTIFICATION_ID_BUS_TRACKING = 1001;
    public static final int NOTIFICATION_ID_ARRIVAL = 1002;
    public static final int NOTIFICATION_ID_DELAY = 1003;
    public static final int NOTIFICATION_ID_EMERGENCY = 1004;

    // Request Codes
    public static final int REQUEST_CODE_LOCATION_PERMISSION = 100;
    public static final int REQUEST_CODE_CAMERA_PERMISSION = 101;
    public static final int REQUEST_CODE_BIOMETRIC_AUTH = 102;
    public static final int REQUEST_CODE_QR_SCAN = 103;

    // API Constants
    public static final String BASE_URL = "https://your-api-endpoint.com/api/";
    public static final int CONNECT_TIMEOUT = 30;
    public static final int READ_TIMEOUT = 30;
    public static final int WRITE_TIMEOUT = 30;

    // Map Constants
    public static final double DEFAULT_LATITUDE = 37.7749;
    public static final double DEFAULT_LONGITUDE = -122.4194;
    public static final float DEFAULT_ZOOM = 15.0f;
    public static final int MAP_PADDING = 100;

    // Trip Status
    public static final String TRIP_STATUS_PENDING = "pending";
    public static final String TRIP_STATUS_IN_PROGRESS = "in_progress";
    public static final String TRIP_STATUS_COMPLETED = "completed";
    public static final String TRIP_STATUS_CANCELLED = "cancelled";

    // Check-in Types
    public static final String CHECKIN_TYPE_GPS = "gps";
    public static final String CHECKIN_TYPE_QR = "qr";
    public static final String CHECKIN_TYPE_MANUAL = "manual";

    // Error Codes
    public static final int ERROR_NETWORK = 1001;
    public static final int ERROR_LOCATION = 1002;
    public static final int ERROR_AUTH = 1003;
    public static final int ERROR_PERMISSION = 1004;
    public static final int ERROR_QR_SCAN = 1005;
    public static final int ERROR_CHECKIN = 1006;

    // Validation
    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MAX_PASSWORD_LENGTH = 20;
    public static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";
    public static final String STUDENT_ID_PATTERN = "^[A-Za-z0-9]{6,12}$";
}

