package com.example.student.models;

import com.google.android.gms.maps.model.LatLng;
import java.util.Date;
import java.util.List;

public class Bus {
    private String busId;
    private String busNumber;
    private String driverName;
    private String driverPhone;
    private String routeId;
    private LatLng currentLocation;
    private double speed;
    private String direction;
    private boolean isMoving;
    private boolean isOnRoute;
    private Date lastUpdated;
    private List<String> studentIds;
    private int capacity;
    private int currentPassengers;

    public Bus() {
        // Default constructor for Firebase
    }

    public Bus(String busId, String busNumber, String driverName, String routeId) {
        this.busId = busId;
        this.busNumber = busNumber;
        this.driverName = driverName;
        this.routeId = routeId;
        this.lastUpdated = new Date();
        this.isMoving = false;
        this.isOnRoute = true;
        this.speed = 0.0;
    }

    // Getters and Setters
    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public LatLng getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(LatLng currentLocation) {
        this.currentLocation = currentLocation;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public boolean isOnRoute() {
        return isOnRoute;
    }

    public void setOnRoute(boolean onRoute) {
        isOnRoute = onRoute;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public List<String> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(List<String> studentIds) {
        this.studentIds = studentIds;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCurrentPassengers() {
        return currentPassengers;
    }

    public void setCurrentPassengers(int currentPassengers) {
        this.currentPassengers = currentPassengers;
    }

    // Additional methods for cross-app integration
    public String getStatus() {
        if (isMoving && isOnRoute) {
            return "On Route";
        } else if (isMoving && !isOnRoute) {
            return "Off Route";
        } else if (!isMoving && isOnRoute) {
            return "Stopped on Route";
        } else {
            return "Stopped";
        }
    }

    public void setStatus(String status) {
        // This method can be used to set status based on string values
        switch (status.toLowerCase()) {
            case "on route":
                this.isMoving = true;
                this.isOnRoute = true;
                break;
            case "off route":
                this.isMoving = true;
                this.isOnRoute = false;
                break;
            case "stopped on route":
                this.isMoving = false;
                this.isOnRoute = true;
                break;
            case "stopped":
            default:
                this.isMoving = false;
                this.isOnRoute = false;
                break;
        }
    }
}

