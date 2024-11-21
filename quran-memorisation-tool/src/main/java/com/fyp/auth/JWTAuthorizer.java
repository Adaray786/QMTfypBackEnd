package com.fyp.auth;

import io.dropwizard.auth.Authorizer;
import com.fyp.cli.User;


public class JWTAuthorizer implements Authorizer<User> {
    @Override
    public boolean authorize(User user, String role){
        return "Admin".equals(user.getRole().getRole_name());
    }
}