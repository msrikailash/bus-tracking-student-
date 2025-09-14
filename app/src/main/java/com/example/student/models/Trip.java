package com.example.student.models;

import java.util.Date;

public class Trip {
    private String tripId;
    private String studentId;
    private String busId;
    private String stopId;
    private Date checkInTime;
    private Date checkOutTime;
    private String checkInLocation;
    private String checkOutLocation;
    private boolean isCompleted;
    private String notes;
    private Date createdAt;

    public Trip() {
        // Default constructor for Firebase
    }

    public Trip(String tripId, String studentId, String busId, String stopId) {
        this.tripId = tripId;
        this.studentId = studentId;
        this.busId = busId;
        this.stopId = stopId;
        this.isCompleted = false;
        this.createdAt = new Date();
    }

    // Getters and Setters
    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public Date getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(Date checkInTime) {
        this.checkInTime = checkInTime;
    }

    public Date getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(Date checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public String getCheckInLocation() {
        return checkInLocation;
    }

    public void setCheckInLocation(String checkInLocation) {
        this.checkInLocation = checkInLocation;
    }

    public String getCheckOutLocation() {
        return checkOutLocation;
    }

    public void setCheckOutLocation(String checkOutLocation) {
        this.checkOutLocation = checkOutLocation;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    // Helper method to calculate trip duration
    public long getTripDuration() {
        if (checkInTime != null && checkOutTime != null) {
            return checkOutTime.getTime() - checkInTime.getTime();
        }
        return 0;
    }
}

