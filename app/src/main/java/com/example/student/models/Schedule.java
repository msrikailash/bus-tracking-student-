package com.example.student.models;

import java.util.Date;

public class Schedule {
    private String scheduleId;
    private String routeId;
    private String stopId;
    private String dayOfWeek;
    private Date pickupTime;
    private Date dropoffTime;
    private boolean isActive;
    private Date createdAt;
    private Date updatedAt;
    private String notes;

    public Schedule() {
        // Default constructor for Firebase
    }

    public Schedule(String scheduleId, String routeId, String stopId, String dayOfWeek, Date pickupTime, Date dropoffTime) {
        this.scheduleId = scheduleId;
        this.routeId = routeId;
        this.stopId = stopId;
        this.dayOfWeek = dayOfWeek;
        this.pickupTime = pickupTime;
        this.dropoffTime = dropoffTime;
        this.isActive = true;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    // Getters and Setters
    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Date getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(Date pickupTime) {
        this.pickupTime = pickupTime;
    }

    public Date getDropoffTime() {
        return dropoffTime;
    }

    public void setDropoffTime(Date dropoffTime) {
        this.dropoffTime = dropoffTime;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // Day of week constants
    public static final String MONDAY = "monday";
    public static final String TUESDAY = "tuesday";
    public static final String WEDNESDAY = "wednesday";
    public static final String THURSDAY = "thursday";
    public static final String FRIDAY = "friday";
    public static final String SATURDAY = "saturday";
    public static final String SUNDAY = "sunday";
}

