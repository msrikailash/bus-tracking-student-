package com.example.student.models;

import java.util.Date;

public class Notification {
    private String notificationId;
    private String studentId;
    private String title;
    private String message;
    private String type;
    private boolean isRead;
    private Date createdAt;
    private String actionUrl;
    private String priority;

    public Notification() {
        // Default constructor for Firebase
    }

    public Notification(String notificationId, String studentId, String title, String message, String type) {
        this.notificationId = notificationId;
        this.studentId = studentId;
        this.title = title;
        this.message = message;
        this.type = type;
        this.isRead = false;
        this.createdAt = new Date();
        this.priority = "normal";
    }

    // Getters and Setters
    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    // Notification types
    public static final String TYPE_BUS_ARRIVAL = "bus_arrival";
    public static final String TYPE_BUS_DELAY = "bus_delay";
    public static final String TYPE_EMERGENCY = "emergency";
    public static final String TYPE_SCHEDULE_CHANGE = "schedule_change";
    public static final String TYPE_CHECKIN_REMINDER = "checkin_reminder";

    // Priority levels
    public static final String PRIORITY_HIGH = "high";
    public static final String PRIORITY_NORMAL = "normal";
    public static final String PRIORITY_LOW = "low";
}

