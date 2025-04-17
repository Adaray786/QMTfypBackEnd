package com.fyp.api;

import com.fyp.cli.AuthRole;
import com.fyp.cli.User;
import com.fyp.client.FailedToGetUserByIdException;
import com.fyp.client.FailedToSearchUsersException;
import com.fyp.db.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserDao userDao;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userDao = mock(UserDao.class);
        userService = new UserService(userDao);
    }

    @Test
    void searchUsers_success() throws Exception {
        AuthRole userRole = new AuthRole(1, "USER");

        List<User> mockUsers = Arrays.asList(
                new User(1, "ali@example.com", userRole, "hashed123"),
                new User(2, "amina@example.com", userRole, "hashed456")
        );

        when(userDao.searchUsers("a", 10)).thenReturn(mockUsers);

        List<User> result = userService.searchUsers("a", 10);

        assertEquals(2, result.size());
        assertEquals("ali@example.com", result.get(0).getEmail());
        assertEquals("amina@example.com", result.get(1).getEmail());
        assertEquals("USER", result.get(0).getRole().getRole_name());
        verify(userDao).searchUsers("a", 10);
    }

    @Test
    void searchUsers_throwsFailedToSearchUsersException() throws Exception {
        when(userDao.searchUsers("fail", 5)).thenThrow(new SQLException("DB down"));

        assertThrows(FailedToSearchUsersException.class, () ->
                userService.searchUsers("fail", 5));
    }

    @Test
    void getUserById_success() throws Exception {
        AuthRole adminRole = new AuthRole(2, "ADMIN");
        User mockUser = new User(3, "fatima@example.com", adminRole, "secretHash");

        when(userDao.getUserById(3)).thenReturn(mockUser);

        User result = userService.getUserById(3);

        assertNotNull(result);
        assertEquals(3, result.getUserId());
        assertEquals("fatima@example.com", result.getEmail());
        assertEquals("ADMIN", result.getRole().getRole_name());
        assertEquals(2, result.getRole().getRoleID());
        verify(userDao).getUserById(3);
    }

    @Test
    void getUserById_throwsFailedToGetUserByIdException() throws Exception {
        when(userDao.getUserById(999)).thenThrow(new SQLException("Not found"));

        assertThrows(FailedToGetUserByIdException.class, () ->
                userService.getUserById(999));
    }
}
