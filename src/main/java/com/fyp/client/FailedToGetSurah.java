package com.fyp.client;

public class FailedToGetSurah extends Exception {
    @Override
    public String getMessage(){
        return "Failed to get Surah";
    }
}
