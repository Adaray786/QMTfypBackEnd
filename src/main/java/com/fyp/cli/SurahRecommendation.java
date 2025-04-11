package com.fyp.cli;

public class SurahRecommendation {
    private int surahId;
    private String surahName;
    private String recommendationType; // "REVISION" or "NEW"
    private double priorityScore; // Higher score means higher priority
    private String reason; // Explanation for the recommendation

    public SurahRecommendation() {}

    public SurahRecommendation(int surahId, String surahName, String recommendationType, 
                             double priorityScore, String reason) {
        this.surahId = surahId;
        this.surahName = surahName;
        this.recommendationType = recommendationType;
        this.priorityScore = priorityScore;
        this.reason = reason;
    }

    // Getters and Setters
    public int getSurahId() {
        return surahId;
    }

    public void setSurahId(int surahId) {
        this.surahId = surahId;
    }

    public String getSurahName() {
        return surahName;
    }

    public void setSurahName(String surahName) {
        this.surahName = surahName;
    }

    public String getRecommendationType() {
        return recommendationType;
    }

    public void setRecommendationType(String recommendationType) {
        this.recommendationType = recommendationType;
    }

    public double getPriorityScore() {
        return priorityScore;
    }

    public void setPriorityScore(double priorityScore) {
        this.priorityScore = priorityScore;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
} 