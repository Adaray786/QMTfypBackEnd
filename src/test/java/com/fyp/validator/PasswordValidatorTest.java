package com.fyp.validator;

import com.fyp.cli.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class PasswordValidatorTest {
    PasswordValidator passwordValidator = new PasswordValidator();

    @Test
    void passwordValidator_ShouldReturnErrorString_WhenPasswordIsLessThan8Characters(){
        RegisterRequest registerRequest = new RegisterRequest("user2@user.com", "pass", 1);
        String result = passwordValidator.validateUser(registerRequest);

        assertEquals("Password must be at least 8 characters long", result, "Expected an error message for a short password");
    }

    @Test
    void passwordValidator_ShouldReturnErrorString_WhenPasswordHasNoUpperCaseLetters(){
        RegisterRequest registerRequest = new RegisterRequest("user2@user.com", "password", 1);
        String result = passwordValidator.validateUser(registerRequest);

        assertEquals("Password must contain at least one upper case letter.", result, "Expected an error message for a wrong password");
    }

    @Test
    void passwordValidator_ShouldReturnErrorString_WhenPasswordHasNoLowerCaseLetters(){
        RegisterRequest registerRequest = new RegisterRequest("user2@user.com", "PASSWORD", 1);
        String result = passwordValidator.validateUser(registerRequest);

        assertEquals("Password must contain at least one lower case letter.", result, "Expected an error message for a wrong password");
    }

    @Test
    void passwordValidator_ShouldReturnErrorString_WhenPasswordHasSpecialCharacters(){
        RegisterRequest registerRequest = new RegisterRequest("user2@user.com", "Password",1);
        String result = passwordValidator.validateUser(registerRequest);

        assertEquals("Password must contain at least one special character (@#$%^&+=)."
                , result, "Expected an error message for a wrong password");
    }
}
