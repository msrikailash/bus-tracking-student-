package com.example.student.models;

import java.util.Date;
import java.util.List;

public class Supervisor {
    private String supervisorId;
    private String name;
    private String email;
    private String phone;
    private String department;
    private List<String> assignedRoutes;
    private List<String> assignedDrivers;
    private boolean isActive;
    private Date lastLogin;
    private Date createdAt;
    private Date updatedAt;
    private List<String> permissions;
    private String emergencyContact;
    private String profileImageUrl;
    private String workingHours;
    private String timezone;

    public Supervisor() {
        // Default constructor for Firebase
    }

    public Supervisor(String supervisorId, String name, String email, String phone, String department) {
        this.supervisorId = supervisorId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.isActive = true;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    // Getters and Setters
    public String getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(String supervisorId) {
        this.supervisorId = supervisorId;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<String> getAssignedRoutes() {
        return assignedRoutes;
    }

    public void setAssignedRoutes(List<String> assignedRoutes) {
        this.assignedRoutes = assignedRoutes;
    }

    public List<String> getAssignedDrivers() {
        return assignedDrivers;
    }

    public void setAssignedDrivers(List<String> assignedDrivers) {
        this.assignedDrivers = assignedDrivers;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
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

    public String getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}
