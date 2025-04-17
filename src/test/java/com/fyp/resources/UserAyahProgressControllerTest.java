package com.fyp.resources;

import com.fyp.api.UserAyahProgressService;
import com.fyp.cli.User;
import com.fyp.cli.UserAyahProgress;
import com.fyp.client.FailedToCheckAyahProgressException;
import com.fyp.client.FailedToGetAyahProgress;
import com.fyp.client.FailedToInsertAyahProgress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserAyahProgressControllerTest {

    private UserAyahProgressService service;
    private UserAyahProgressController controller;
    private User mockUser;

    @BeforeEach
    void setUp() {
        service = mock(UserAyahProgressService.class);
        controller = new UserAyahProgressController(service);
        mockUser = new User(1, "user@email.com", null);
    }

    @Test
    void upsertAyahProgress_ShouldReturn200_WhenSuccessful() throws Exception, FailedToCheckAyahProgressException {
        UserAyahProgress progress = new UserAyahProgress(1, 1, 1, true);
        doNothing().when(service).upsertUserAyahProgress(1, 1, true);

        Response response = controller.upsertAyahProgress(mockUser, progress);

        assertEquals(200, response.getStatus());
        assertEquals("Ayah progress updated successfully", response.getEntity());
    }

    @Test
    void upsertAyahProgress_ShouldReturn403_WhenUnauthorizedUser() {
        // Progress with userId = 2 (different from mockUser = 1)
        UserAyahProgress progress = new UserAyahProgress(2, 2, 1, true);

        // Don't mock service call – this should not be reached
        // so don't do: doNothing().when(service)...

        Response response = controller.upsertAyahProgress(mockUser, progress);

        assertEquals(403, response.getStatus());
        assertEquals("You are not authorized to update this user's Ayah progress", response.getEntity());
    }


    @Test
    void upsertAyahProgress_ShouldReturn500_WhenFailedToInsert() throws Exception, FailedToCheckAyahProgressException {
        UserAyahProgress progress = new UserAyahProgress(1, 1, 1, true);
        doThrow(FailedToInsertAyahProgress.class).when(service).upsertUserAyahProgress(1, 1, true);

        Response response = controller.upsertAyahProgress(mockUser, progress);

        assertEquals(500, response.getStatus());
        assertEquals("Failed to update Ayah progress", response.getEntity());
    }

    @Test
    void getAyahProgress_ShouldReturn200_WhenFound() throws Exception {
        UserAyahProgress progress = new UserAyahProgress(1, 1, 1, true);
        when(service.getUserAyahProgress(1, 1)).thenReturn(progress);

        Response response = controller.getAyahProgress(1, 1);

        assertEquals(200, response.getStatus());
        assertEquals(progress, response.getEntity());
    }

    @Test
    void getAyahProgress_ShouldReturn404_WhenNotFound() throws Exception {
        when(service.getUserAyahProgress(1, 1)).thenReturn(null);

        Response response = controller.getAyahProgress(1, 1);

        assertEquals(404, response.getStatus());
        assertEquals("Ayah progress not found", response.getEntity());
    }

    @Test
    void getAyahProgress_ShouldReturn500_WhenError() throws Exception {
        when(service.getUserAyahProgress(1, 1)).thenThrow(FailedToGetAyahProgress.class);

        Response response = controller.getAyahProgress(1, 1);

        assertEquals(500, response.getStatus());
        assertEquals("Failed to retrieve Ayah progress", response.getEntity());
    }

    @Test
    void getAllAyahProgressByUser_ShouldReturn200_WhenSuccessful() throws Exception {
        List<UserAyahProgress> list = new ArrayList<>();
        list.add(new UserAyahProgress(1, 1, 1, true));

        when(service.getAllAyahProgressByUser(1)).thenReturn(list);

        Response response = controller.getAllAyahProgressByUser(1);

        assertEquals(200, response.getStatus());
        assertEquals(list, response.getEntity());
    }

    @Test
    void getAllAyahProgressByUser_ShouldReturn500_WhenError() throws Exception {
        when(service.getAllAyahProgressByUser(1)).thenThrow(FailedToGetAyahProgress.class);

        Response response = controller.getAllAyahProgressByUser(1);

        assertEquals(500, response.getStatus());
        assertEquals("Failed to retrieve Ayah progress", response.getEntity());
    }

    @Test
    void getAyahProgressBySurahAndUser_ShouldReturn200_WhenSuccessful() throws Exception, FailedToCheckAyahProgressException {
        List<UserAyahProgress> list = new ArrayList<>();
        list.add(new UserAyahProgress(1, 1, 1, true));

        when(service.getAyahProgressBySurahAndUser(1, 1)).thenReturn(list);

        Response response = controller.getAyahProgressBySurahAndUser(mockUser, 1, 1);

        assertEquals(200, response.getStatus());
        assertEquals(list, response.getEntity());
    }

    @Test
    void getAyahProgressBySurahAndUser_ShouldReturn401_WhenNotAuthenticated() {
        Response response = controller.getAyahProgressBySurahAndUser(null, 1, 1);

        assertEquals(401, response.getStatus());
        assertEquals("Authentication required", response.getEntity());
    }

    @Test
    void getAyahProgressBySurahAndUser_ShouldReturn403_WhenNotAuthorized() {
        User otherUser = new User(2, "other@email.com", null);

        Response response = controller.getAyahProgressBySurahAndUser(otherUser, 1, 1);

        assertEquals(403, response.getStatus());
        assertEquals("You are not authorized to access this user's Surah progress", response.getEntity());
    }

    @Test
    void getAyahProgressBySurahAndUser_ShouldReturn500_WhenSQLException() throws Exception, FailedToCheckAyahProgressException {
        when(service.getAyahProgressBySurahAndUser(1, 1)).thenThrow(SQLException.class);

        Response response = controller.getAyahProgressBySurahAndUser(mockUser, 1, 1);

        assertEquals(500, response.getStatus());
        assertEquals("Failed to fetch Ayah progress for the given User and Surah", response.getEntity());
    }
}
