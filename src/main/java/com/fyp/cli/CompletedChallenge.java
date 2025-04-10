package com.fyp.cli;

public class CompletedChallenge {
    private int completedChallengeId;
    private int challengerId;
    private int challengedId;
    private int surahId;
    private int winnerId;
    private String startedAt;
    private String completedAt;
    private String otherUserEmail;

    public CompletedChallenge() {}

    public CompletedChallenge(int completedChallengeId, int challengerId, int challengedId, 
                            int surahId, int winnerId, String startedAt, String completedAt,
                            String otherUserEmail) {
        this.completedChallengeId = completedChallengeId;
        this.challengerId = challengerId;
        this.challengedId = challengedId;
        this.surahId = surahId;
        this.winnerId = winnerId;
        this.startedAt = startedAt;
        this.completedAt = completedAt;
        this.otherUserEmail = otherUserEmail;
    }

    // Getters and Setters
    public int getCompletedChallengeId() {
        return completedChallengeId;
    }

    public void setCompletedChallengeId(int completedChallengeId) {
        this.completedChallengeId = completedChallengeId;
    }

    public int getChallengerId() {
        return challengerId;
    }

    public void setChallengerId(int challengerId) {
        this.challengerId = challengerId;
    }

    public int getChallengedId() {
        return challengedId;
    }

    public void setChallengedId(int challengedId) {
        this.challengedId = challengedId;
    }

    public int getSurahId() {
        return surahId;
    }

    public void setSurahId(int surahId) {
        this.surahId = surahId;
    }

    public int getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(int winnerId) {
        this.winnerId = winnerId;
    }

    public String getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(String startedAt) {
        this.startedAt = startedAt;
    }

    public String getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(String completedAt) {
        this.completedAt = completedAt;
    }

    public String getOtherUserEmail() {
        return otherUserEmail;
    }

    public void setOtherUserEmail(String otherUserEmail) {
        this.otherUserEmail = otherUserEmail;
    }
} 