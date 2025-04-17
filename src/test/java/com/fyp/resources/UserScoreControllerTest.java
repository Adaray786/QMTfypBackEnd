package com.fyp.resources;

import com.fyp.api.UserScoreService;
import com.fyp.cli.User;
import com.fyp.client.FailedToGetFriendsScoresException;
import com.fyp.client.FailedToGetUserScoreException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserScoreControllerTest {

    private UserScoreService userScoreService;
    private UserScoreController controller;
    private User mockUser;

    @BeforeEach
    void setUp() {
        userScoreService = mock(UserScoreService.class);
        controller = new UserScoreController(userScoreService);
        mockUser = new User(1, "test@email.com", null);
    }

    @Test
    void getUserScore_ShouldReturn200_WhenSuccessful() throws FailedToGetUserScoreException {
        when(userScoreService.getUserScore(1)).thenReturn(75);

        Response response = controller.getUserScore(mockUser);

        assertEquals(200, response.getStatus());
        assertEquals(75, response.getEntity());
    }

    @Test
    void getUserScore_ShouldThrowRuntimeException_WhenServiceFails() throws FailedToGetUserScoreException {
        when(userScoreService.getUserScore(1))
                .thenThrow(new FailedToGetUserScoreException("Could not retrieve score", new RuntimeException()));

        assertThrows(RuntimeException.class, () -> controller.getUserScore(mockUser));
    }

    @Test
    void getFriendsScores_ShouldReturn200_WhenSuccessful() throws FailedToGetFriendsScoresException {
        Map<Integer, Integer> scores = new HashMap<>();
        scores.put(2, 50);
        scores.put(3, 80);

        when(userScoreService.getFriendsScores(1)).thenReturn(scores);

        Response response = controller.getFriendsScores(mockUser);

        assertEquals(200, response.getStatus());
        assertEquals(scores, response.getEntity());
    }

    @Test
    void getFriendsScores_ShouldReturn500_WhenServiceFails() throws FailedToGetFriendsScoresException {
        when(userScoreService.getFriendsScores(1))
                .thenThrow(new FailedToGetFriendsScoresException("Failed", new RuntimeException()));

        Response response = controller.getFriendsScores(mockUser);

        assertEquals(500, response.getStatus());
        assertEquals("Error retrieving friends' scores.", response.getEntity());
    }
}
