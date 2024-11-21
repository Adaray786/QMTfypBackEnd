package com.fyp.validator;

import com.fyp.cli.RegisterRequest;

public class PasswordValidator {
    public String validateUser(RegisterRequest user) {
        String password = user.getPassword();

        if (password.length() < 8) {
            return "Password must be at least 8 characters long";
        }

        if (!password.matches(".*[A-Z].*")) {
            return "Password must contain at least one upper case letter.";
        }

        if (!password.matches(".*[a-z].*")) {
            return "Password must contain at least one lower case letter.";
        }

        // Check if password contains at least one special character
        if (!password.matches(".*[@#$%^&+=*].*")) {
            return "Password must contain at least one special character (@#$%^&+=).";
        }

        return "";
    }

}