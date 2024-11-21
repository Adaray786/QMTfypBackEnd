package com.fyp.validator;

import com.fyp.cli.RegisterRequest;

public class EmailValidator {

    public String validateUserEmail(RegisterRequest user) {
        String email = user.getUsername();

        String emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!email.matches(emailPattern)) {
            return "Invalid email address";
        }

        return "";
    }

}