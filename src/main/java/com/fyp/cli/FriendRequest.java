package com.fyp.cli;

public class FriendRequest {
    private int requestId;
    private int senderId;
    private String senderName;  // ✅ Add sender's name

    public FriendRequest(int requestId, int senderId, String senderName) {
        this.requestId = requestId;
        this.senderId = senderId;
        this.senderName = senderName;
    }

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

    public String getSenderName() {  // ✅ Getter for sender name
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
