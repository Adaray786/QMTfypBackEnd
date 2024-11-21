package com.fyp.client;

public class FailedToGenerateTokenException extends  Throwable{
    @Override
    public String getMessage(){
        return "Failed to generate token";
    }
}