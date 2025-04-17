package com.fyp.validator;

import com.fyp.cli.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class EmailValidatorTest {
    EmailValidator emailValidator = new EmailValidator();
    @Test
    void passwordValidator_ShouldReturnErrorString_WhenEmailIsWrongFormat(){
        RegisterRequest registerRequest = new RegisterRequest("invalidEmail", "Password$", 1);
        String result = emailValidator.validateUserEmail(registerRequest);

        assertEquals("Invalid email address", result, "Expected an error message for a wrong email");
    }

    @Test
    void passwordValidator_ShouldReturnEmptyString_WhenEmailAndPasswordIsCorrect(){
        RegisterRequest registerRequest = new RegisterRequest("user2@user.com", "Password$", 1);
        String result = emailValidator.validateUserEmail(registerRequest);

        assertEquals("", result, "Expected an empty string for a valid password");
    }


}
