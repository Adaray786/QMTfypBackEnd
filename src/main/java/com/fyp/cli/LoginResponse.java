package com.fyp.cli;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponse {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }





    @JsonCreator
    public LoginResponse(
            @JsonProperty("token") String token
    ) {
        this.token = token;
    }
}