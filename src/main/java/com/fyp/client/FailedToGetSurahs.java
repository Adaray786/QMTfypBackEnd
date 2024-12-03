package com.fyp.client;

public class FailedToGetSurahs extends Exception {
    @Override
    public String getMessage(){
        return "Failed to get Surahs";
    }
}
