package com.fyp.cli;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterRequest {
    private String username;

    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    private int role;


    @JsonCreator
    public RegisterRequest(
            @JsonProperty("username") String username,
            @JsonProperty("password") String password,
            @JsonProperty("role") int role
    ) {
        this.username = username;
        this.password = password;
        this.role = role;
    }


}