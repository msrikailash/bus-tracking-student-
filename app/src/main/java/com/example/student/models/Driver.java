package com.example.student.models;

import java.util.Date;
import java.util.List;

public class Driver {
    private String driverId;
    private String name;
    private String email;
    private String phone;
    private String licenseNumber;
    private String assignedBusId;
    private String routeId;
    private boolean isActive;
    private boolean isOnDuty;
    private Date lastLogin;
    private Date createdAt;
    private Date updatedAt;
    private List<String> permissions;
    private String supervisorId;
    private String emergencyContact;
    private String profileImageUrl;

    public Driver() {
        // Default constructor for Firebase
    }

    public Driver(String driverId, String name, String email, String phone, String licenseNumber) {
        this.driverId = driverId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.licenseNumber = licenseNumber;
        this.isActive = true;
        this.isOnDuty = false;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    // Getters and Setters
    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getAssignedBusId() {
        return assignedBusId;
    }

    public void setAssignedBusId(String assignedBusId) {
        this.assignedBusId = assignedBusId;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isOnDuty() {
        return isOnDuty;
    }

    public void setOnDuty(boolean onDuty) {
        isOnDuty = onDuty;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
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

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public String getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(String supervisorId) {
        this.supervisorId = supervisorId;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
