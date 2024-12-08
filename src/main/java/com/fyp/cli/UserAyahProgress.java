package com.fyp.cli;

public class UserAyahProgress {
    private int progressId;    // ProgressID field
    private int userId;        // UserID field
    private int ayahId;        // AyahID field
    private boolean isMemorized; // Is_Memorized field

    // Constructors
    public UserAyahProgress() {
    }

    public UserAyahProgress(int progressId, int userId, int ayahId, boolean isMemorized) {
        this.progressId = progressId;
        this.userId = userId;
        this.ayahId = ayahId;
        this.isMemorized = isMemorized;
    }

    // Getters and Setters
    public int getProgressId() {
        return progressId;
    }

    public void setProgressId(int progressId) {
        this.progressId = progressId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAyahId() {
        return ayahId;
    }

    public void setAyahId(int ayahId) {
        this.ayahId = ayahId;
    }

    public boolean isMemorized() {
        return isMemorized;
    }

    public void setMemorized(boolean memorized) {
        isMemorized = memorized;
    }

    // toString() for Debugging
    @Override
    public String toString() {
        return "UserAyahProgress{" +
                "progressId=" + progressId +
                ", userId=" + userId +
                ", ayahId=" + ayahId +
                ", isMemorized=" + isMemorized +
                '}';
    }
}
