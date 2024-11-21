package com.fyp.client;

public class FailedToGetRolesException extends Exception {
    @Override
    public String getMessage(){
        return "Failed to get available roles";
    }
}