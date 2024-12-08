package com.fyp.cli;

public class UserSurahProgress {
    private int progressId;    // ProgressID field
    private int userId;        // UserID field
    private int surahId;       // SurahID field
    private boolean isMemorized; // Is_Memorized field

    // Constructors
    public UserSurahProgress() {
    }

    public UserSurahProgress(int progressId, int userId, int surahId, boolean isMemorized) {
        this.progressId = progressId;
        this.userId = userId;
        this.surahId = surahId;
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

    public int getSurahId() {
        return surahId;
    }

    public void setSurahId(int surahId) {
        this.surahId = surahId;
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
        return "UserSurahProgress{" +
                "progressId=" + progressId +
                ", userId=" + userId +
                ", surahId=" + surahId +
                ", isMemorized=" + isMemorized +
                '}';
    }
}
