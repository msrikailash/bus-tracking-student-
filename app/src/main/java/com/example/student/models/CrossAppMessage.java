package com.example.student.models;

import java.util.Date;
import java.util.Map;

public class CrossAppMessage {
    private String messageId;
    private String senderId;
    private String senderType; // "student", "driver", "supervisor"
    private String receiverId;
    private String receiverType;
    private String messageType; // "location_update", "checkin", "emergency", "notification", "status_update"
    private String title;
    private String content;
    private Map<String, Object> data;
    private String priority; // "low", "medium", "high", "urgent"
    private boolean isRead;
    private Date timestamp;
    private Date readAt;
    private String busId;
    private String routeId;
    private String studentId;

    public CrossAppMessage() {
        // Default constructor for Firebase
    }

    public CrossAppMessage(String messageId, String senderId, String senderType, 
                          String receiverId, String receiverType, String messageType) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.senderType = senderType;
        this.receiverId = receiverId;
        this.receiverType = receiverType;
        this.messageType = messageType;
        this.timestamp = new Date();
        this.isRead = false;
        this.priority = "medium";
    }

    // Getters and Setters
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderType() {
        return senderType;
    }

    public void setSenderType(String senderType) {
        this.senderType = senderType;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(String receiverType) {
        this.receiverType = receiverType;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
        if (read && readAt == null) {
            readAt = new Date();
        }
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date getReadAt() {
        return readAt;
    }

    public void setReadAt(Date readAt) {
        this.readAt = readAt;
    }

    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}
