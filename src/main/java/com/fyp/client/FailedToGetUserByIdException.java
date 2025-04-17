package com.fyp.client;

public class FailedToGetUserByIdException extends Exception {
    public FailedToGetUserByIdException(String message, Throwable cause) {
        super(message, cause);
    }
}
