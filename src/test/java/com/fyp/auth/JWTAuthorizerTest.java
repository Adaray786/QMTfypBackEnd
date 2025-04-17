package com.fyp.auth;

import com.fyp.cli.AuthRole;
import com.fyp.cli.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class JWTAuthorizerTest {
    private JWTAuthorizer jwtAuthorizer;

    @BeforeEach
    public void setUp() {
        jwtAuthorizer = new JWTAuthorizer();
    }

    @Test
    void jwtAuthorizer_ShouldReturnTrue_WhenUserIsAdmin(){
        AuthRole role = new AuthRole(1, "Admin");
        User adminUser = new User(1, "email@email.com", role);

        assertTrue(jwtAuthorizer.authorize(adminUser, role.toString()));
    }

    @Test
    void jwtAuthorizer_ShouldReturnFalse_WhenUserIsNotAdmin(){
        AuthRole role = new AuthRole(1, "Employee");
        User user = new User(1, "user@user.com", role);

        assertFalse(jwtAuthorizer.authorize(user, role.toString()));
    }
}
