package com.fyp.cli;

public class ChallengeRequest {
    private int requestId;
    private int senderId;
    private int receiverId;
    private int surahId;
    private String status;
    private String sentAt;
    private String senderEmail;

    public ChallengeRequest() {}

    public ChallengeRequest(int requestId, int senderId, int receiverId, int surahId, String status, String sentAt, String senderEmail) {
        this.requestId = requestId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.surahId = surahId;
        this.status = status;
        this.sentAt = sentAt;
        this.senderEmail = senderEmail;
    }

    // Getters and Setters
    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public int getSurahId() {
        return surahId;
    }

    public void setSurahId(int surahId) {
        this.surahId = surahId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSentAt() {
        return sentAt;
    }

    public void setSentAt(String sentAt) {
        this.sentAt = sentAt;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }
} 