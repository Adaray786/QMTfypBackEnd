package com.fyp.resources;

import com.fyp.api.UserSurahProgressService;
import com.fyp.cli.User;
import com.fyp.cli.UserSurahProgress;
import com.fyp.client.FailedToGetSurahProgress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserSurahProgressControllerTest {

    private UserSurahProgressService service;
    private UserSurahProgressController controller;
    private User mockUser;

    @BeforeEach
    void setUp() {
        service = mock(UserSurahProgressService.class);
        controller = new UserSurahProgressController(service);
        mockUser = new User(1, "user@test.com", null);
    }

    @Test
    void upsertSurahProgress_ShouldReturn200_WhenValid() throws Exception {
        UserSurahProgress progress = new UserSurahProgress(1, 1, 1, true, "2023-01-01");

        doNothing().when(service).createOrUpdateSurahProgress(1, 1);

        Response response = controller.upsertSurahProgress(mockUser, progress);

        assertEquals(200, response.getStatus());
        assertEquals("Surah progress updated successfully", response.getEntity());
    }

    @Test
    void upsertSurahProgress_ShouldReturn401_WhenUserIsNull() {
        UserSurahProgress progress = new UserSurahProgress(1, 1, 1, true, "2023-01-01");

        Response response = controller.upsertSurahProgress(null, progress);

        assertEquals(401, response.getStatus());
        assertEquals("Authentication required", response.getEntity());
    }

    @Test
    void upsertSurahProgress_ShouldReturn403_WhenUnauthorized() {
        UserSurahProgress progress = new UserSurahProgress(2, 2, 2, true, "2023-01-01");

        Response response = controller.upsertSurahProgress(mockUser, progress);

        assertEquals(403, response.getStatus());
        assertEquals("You are not authorized to access this user's Surah progress", response.getEntity());
    }

    @Test
    void upsertSurahProgress_ShouldReturn500_OnFailure() throws Exception {
        UserSurahProgress progress = new UserSurahProgress(1, 1, 1, true, "2023-01-01");

        doThrow(new FailedToGetSurahProgress())
                .when(service).createOrUpdateSurahProgress(1, 1);

        Response response = controller.upsertSurahProgress(mockUser, progress);

        assertEquals(500, response.getStatus());
        assertEquals("Failed to update Surah progress", response.getEntity());
    }

    @Test
    void getSurahProgress_ShouldReturn200_WhenFound() throws FailedToGetSurahProgress {
        UserSurahProgress progress = new UserSurahProgress(1, 1, 1, true, "2023-01-01");
        when(service.getUserSurahProgress(1, 1)).thenReturn(progress);

        Response response = controller.getSurahProgress(mockUser, 1, 1);

        assertEquals(200, response.getStatus());
        assertEquals(progress, response.getEntity());
    }

    @Test
    void getSurahProgress_ShouldReturn401_WhenUnauthenticated() {
        Response response = controller.getSurahProgress(null, 1, 1);

        assertEquals(401, response.getStatus());
        assertEquals("Authentication required", response.getEntity());
    }

    @Test
    void getSurahProgress_ShouldReturn403_WhenUnauthorized() {
        Response response = controller.getSurahProgress(new User(2, "other@test.com", null), 1, 1);

        assertEquals(403, response.getStatus());
        assertEquals("You are not authorized to access this user's Surah progress", response.getEntity());
    }

    @Test
    void getSurahProgress_ShouldReturn404_WhenNotFound() throws FailedToGetSurahProgress {
        when(service.getUserSurahProgress(1, 1)).thenReturn(null);

        Response response = controller.getSurahProgress(mockUser, 1, 1);

        assertEquals(404, response.getStatus());
        assertEquals("Surah progress not found", response.getEntity());
    }

    @Test
    void getSurahProgress_ShouldReturn500_OnFailure() throws FailedToGetSurahProgress {
        when(service.getUserSurahProgress(1, 1))
                .thenThrow(new FailedToGetSurahProgress());

        Response response = controller.getSurahProgress(mockUser, 1, 1);

        assertEquals(500, response.getStatus());
        assertEquals("Failed to retrieve Surah progress", response.getEntity());
    }


    @Test
    void getAllSurahProgressByUser_ShouldReturn200_WhenSuccessful() throws FailedToGetSurahProgress {
        List<UserSurahProgress> list = new ArrayList<>();
        list.add(new UserSurahProgress(1, 1, 1, true, "2023-01-01"));

        when(service.getAllSurahProgressByUser(1)).thenReturn(list);

        Response response = controller.getAllSurahProgressByUser(mockUser, 1);

        assertEquals(200, response.getStatus());
        assertEquals(list, response.getEntity());
    }

    @Test
    void getAllSurahProgressByUser_ShouldReturn401_WhenUnauthenticated() {
        Response response = controller.getAllSurahProgressByUser(null, 1);

        assertEquals(401, response.getStatus());
        assertEquals("Authentication required", response.getEntity());
    }

    @Test
    void getAllSurahProgressByUser_ShouldReturn403_WhenUnauthorized() {
        Response response = controller.getAllSurahProgressByUser(new User(2, "other@test.com", null), 1);

        assertEquals(403, response.getStatus());
        assertEquals("You are not authorized to access this user's Surah progress", response.getEntity());
    }

    @Test
    void getAllSurahProgressByUser_ShouldReturn500_OnFailure() throws FailedToGetSurahProgress {
        when(service.getAllSurahProgressByUser(1))
                .thenThrow(new FailedToGetSurahProgress());

        Response response = controller.getAllSurahProgressByUser(mockUser, 1);

        assertEquals(500, response.getStatus());
        assertEquals("Failed to retrieve Surah progress", response.getEntity());
    }
}
