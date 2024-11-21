package com.fyp.client;

public class FailedToGetUserId extends Throwable {
    @Override
    public String getMessage(){
        return "Failed to get userId from db";
    }
}