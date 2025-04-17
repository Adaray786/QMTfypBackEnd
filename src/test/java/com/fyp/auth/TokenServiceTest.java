package com.fyp.auth;

import com.fyp.cli.AuthRole;
import com.fyp.cli.User;
import com.fyp.db.AuthDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {
    private TokenService tokenService;
    private AuthDao authDao;

    @BeforeEach
    public void setUp() {
        authDao = Mockito.mock(AuthDao.class);
        tokenService = new TokenService(authDao);
    }


    @Test
    public void decodeToken_ShouldDecodeToken_IfValidToken() throws SQLException {
        User expectedUser = new User(1, "test@example.com", new AuthRole(1, "Admin"));
        String validToken = tokenService.generateToken(expectedUser);

        when(authDao.getUserByEmail(anyString())).thenReturn(expectedUser);

        Optional<User> decodedUser = tokenService.decodeToken(validToken);

        assertTrue(decodedUser.isPresent());
        assertEquals(expectedUser, decodedUser.get());

        verify(authDao, times(1)).getUserByEmail("test@example.com");
    }

    @Test
    public void decodeToken_ShouldFail_IfInvalidToken(){
        String invalidToken = "invalidToken";

        Optional<User> decodedUser = tokenService.decodeToken(invalidToken);

        assertFalse(decodedUser.isPresent());
    }

    @Test
    public void isValidToken_ShouldReturnTrue_IfTokenValid(){
        User expectedUser = new User(1, "test@example.com", new AuthRole(1, "Admin"));
        String validToken = tokenService.generateToken(expectedUser);

        boolean isValid = tokenService.isValidToken(validToken);

        assertTrue(isValid);
    }

    @Test
    public void isValidToken_ShouldReturnFalse_IfTokenInvalid(){
        String invalidToken = "invalidToken";

        boolean isValid = tokenService.isValidToken(invalidToken);

        assertFalse(isValid);
    }


}
