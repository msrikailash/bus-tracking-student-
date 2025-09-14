package com.example.student.models;

import com.google.android.gms.maps.model.LatLng;
import java.util.Date;
import java.util.List;

public class Route {
    private String routeId;
    private String routeName;
    private String description;
    private List<BusStop> stops;
    private List<LatLng> pathCoordinates;
    private double totalDistance;
    private int estimatedDuration;
    private boolean isActive;
    private Date createdAt;
    private Date updatedAt;

    public Route() {
        // Default constructor for Firebase
    }

    public Route(String routeId, String routeName, List<BusStop> stops) {
        this.routeId = routeId;
        this.routeName = routeName;
        this.stops = stops;
        this.isActive = true;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    // Getters and Setters
    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<BusStop> getStops() {
        return stops;
    }

    public void setStops(List<BusStop> stops) {
        this.stops = stops;
    }

    public List<LatLng> getPathCoordinates() {
        return pathCoordinates;
    }

    public void setPathCoordinates(List<LatLng> pathCoordinates) {
        this.pathCoordinates = pathCoordinates;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public int getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(int estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
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

