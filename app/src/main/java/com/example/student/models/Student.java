package com.example.student.models;

import java.util.Date;

public class Student {
    private String studentId;
    private String name;
    private String email;
    private String grade;
    private String assignedBusId;
    private String parentPhone;
    private String emergencyContact;
    private String profileImageUrl;
    private Date createdAt;
    private Date updatedAt;
    private boolean isActive;

    public Student() {
        // Default constructor for Firebase
    }

    public Student(String studentId, String name, String email, String grade, String assignedBusId) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.grade = grade;
        this.assignedBusId = assignedBusId;
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.isActive = true;
    }

    // Getters and Setters
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
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

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getAssignedBusId() {
        return assignedBusId;
    }

    public void setAssignedBusId(String assignedBusId) {
        this.assignedBusId = assignedBusId;
    }

    public String getParentPhone() {
        return parentPhone;
    }

    public void setParentPhone(String parentPhone) {
        this.parentPhone = parentPhone;
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}

