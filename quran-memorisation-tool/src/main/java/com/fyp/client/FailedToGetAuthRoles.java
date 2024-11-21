package com.fyp.client;

public class FailedToGetAuthRoles extends Throwable{
    @Override
    public String getMessage(){
        return "Failed to get auth roles";
    }

}
