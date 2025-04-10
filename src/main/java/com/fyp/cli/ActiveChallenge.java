package com.fyp.cli;

public class ActiveChallenge {
    private int challengeId;
    private int user1Id;
    private int user2Id;
    private int surahId;
    private String startedAt;
    private String otherUserEmail;

    public ActiveChallenge() {}

    public ActiveChallenge(int challengeId, int user1Id, int user2Id, int surahId, String startedAt, String otherUserEmail) {
        this.challengeId = challengeId;
        this.user1Id = user1Id;
        this.user2Id = user2Id;
        this.surahId = surahId;
        this.startedAt = startedAt;
        this.otherUserEmail = otherUserEmail;
    }

    // Getters and Setters
    public int getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(int challengeId) {
        this.challengeId = challengeId;
    }

    public int getUser1Id() {
        return user1Id;
    }

    public void setUser1Id(int user1Id) {
        this.user1Id = user1Id;
    }

    public int getUser2Id() {
        return user2Id;
    }

    public void setUser2Id(int user2Id) {
        this.user2Id = user2Id;
    }

    public int getSurahId() {
        return surahId;
    }

    public void setSurahId(int surahId) {
        this.surahId = surahId;
    }

    public String getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(String startedAt) {
        this.startedAt = startedAt;
    }

    public String getOtherUserEmail() {
        return otherUserEmail;
    }

    public void setOtherUserEmail(String otherUserEmail) {
        this.otherUserEmail = otherUserEmail;
    }
} 