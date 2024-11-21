package com.fyp.client;

public class FailedToGetUserPassword extends Throwable {
    @Override
    public String getMessage()
    {
        return "Failed to get users' password";
    }
}
