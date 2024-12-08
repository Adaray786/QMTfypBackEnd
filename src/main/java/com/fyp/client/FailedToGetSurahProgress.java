package com.fyp.client;

public class FailedToGetSurahProgress extends Exception {
    @Override
    public String getMessage() {
        return "Failed to get surah progress.";
    }
}
