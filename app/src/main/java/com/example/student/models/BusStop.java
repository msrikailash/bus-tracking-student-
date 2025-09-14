package com.example.student.models;

import com.google.android.gms.maps.model.LatLng;
import java.util.Date;

public class BusStop {
    private String stopId;
    private String stopName;
    private String description;
    private LatLng location;
    private String qrCode;
    private int sequence;
    private boolean isActive;
    private Date createdAt;
    private Date updatedAt;

    public BusStop() {
        // Default constructor for Firebase
    }

    public BusStop(String stopId, String stopName, LatLng location, int sequence) {
        this.stopId = stopId;
        this.stopName = stopName;
        this.location = location;
        this.sequence = sequence;
        this.isActive = true;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    // Getters and Setters
    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
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
}

