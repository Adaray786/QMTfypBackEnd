package com.fyp.client;

public class FailedToRegisterException extends Exception {
    @Override
    public String getMessage(){
        return "Failed to register user";
    }
}