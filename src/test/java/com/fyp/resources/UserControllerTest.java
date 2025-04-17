package com.fyp.resources;

import com.fyp.api.UserService;
import com.fyp.cli.User;
import com.fyp.client.FailedToGetUserByIdException;
import com.fyp.client.FailedToSearchUsersException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private UserService userService;
    private UserController userController;
    private User mockUser;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
        mockUser = new User(1, "user@test.com", null);
    }

    // ✅ Test successful search
    @Test
    void searchUsers_ShouldReturn200_WhenSuccessful() throws FailedToSearchUsersException {
        List<User> users = new ArrayList<>();
        users.add(new User(2, "found@user.com", null));

        when(userService.searchUsers("found", 1)).thenReturn(users);

        Response response = userController.searchUsers(mockUser, "found");

        assertEquals(200, response.getStatus());
        assertEquals(users, response.getEntity());
    }

    // ✅ Test exception during search
    @Test
    void searchUsers_ShouldThrowRuntimeException_WhenSearchFails() throws FailedToSearchUsersException {
        when(userService.searchUsers("fail", 1))
                .thenThrow(new FailedToSearchUsersException("Search failed", new RuntimeException()));


        assertThrows(RuntimeException.class, () -> {
            userController.searchUsers(mockUser, "fail");
        });
    }

    // ✅ Test get user by ID - found
    @Test
    void getUserById_ShouldReturn200_WhenUserExists() throws FailedToGetUserByIdException {
        User foundUser = new User(2, "target@user.com", null);
        when(userService.getUserById(2)).thenReturn(foundUser);

        Response response = userController.getUserById(mockUser, 2);

        assertEquals(200, response.getStatus());
        assertEquals(foundUser, response.getEntity());
    }

    // ✅ Test get user by ID - not found
    @Test
    void getUserById_ShouldReturn404_WhenUserNotFound() throws FailedToGetUserByIdException {
        when(userService.getUserById(2)).thenReturn(null);

        Response response = userController.getUserById(mockUser, 2);

        assertEquals(404, response.getStatus());
        assertEquals("User not found", response.getEntity());
    }

    // ✅ Test get user by ID - throws exception
    @Test
    void getUserById_ShouldThrowRuntimeException_WhenServiceFails() throws FailedToGetUserByIdException {
        when(userService.getUserById(2))
                .thenThrow(new FailedToGetUserByIdException("Failed to get user", new RuntimeException()));


        assertThrows(RuntimeException.class, () -> {
            userController.getUserById(mockUser, 2);
        });
    }
}
