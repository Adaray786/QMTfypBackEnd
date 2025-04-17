package com.fyp.api;

import com.fyp.cli.UserSurahProgress;
import com.fyp.client.FailedToGetSurahProgress;
import com.fyp.db.UserSurahProgressDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserSurahProgressServiceTest {

    private UserSurahProgressDao userSurahProgressDao;
    private UserSurahProgressService userSurahProgressService;

    @BeforeEach
    void setUp() {
        userSurahProgressDao = mock(UserSurahProgressDao.class);
        userSurahProgressService = new UserSurahProgressService(userSurahProgressDao);
    }

    @Test
    void createOrUpdateSurahProgress_success() throws Exception {
        doNothing().when(userSurahProgressDao).createOrUpdateSurahProgress(1, 114);

        assertDoesNotThrow(() ->
                userSurahProgressService.createOrUpdateSurahProgress(1, 114));

        verify(userSurahProgressDao).createOrUpdateSurahProgress(1, 114);
    }

    @Test
    void createOrUpdateSurahProgress_throwsSQLException() throws Exception {
        doThrow(new SQLException("DB error"))
                .when(userSurahProgressDao).createOrUpdateSurahProgress(1, 114);

        assertThrows(SQLException.class, () ->
                userSurahProgressService.createOrUpdateSurahProgress(1, 114));
    }

    @Test
    void getUserSurahProgress_success() throws Exception {
        UserSurahProgress progress = new UserSurahProgress(101, 1, 114, true, "2024-04-01 10:00:00");

        when(userSurahProgressDao.getUserSurahProgress(1, 114)).thenReturn(progress);

        UserSurahProgress result = userSurahProgressService.getUserSurahProgress(1, 114);

        assertNotNull(result);
        assertEquals(101, result.getProgressId());
        assertEquals(1, result.getUserId());
        assertEquals(114, result.getSurahId());
        assertTrue(result.isMemorized());
        assertEquals("2024-04-01 10:00:00", result.getLastRevisedAt());
    }

    @Test
    void getUserSurahProgress_throwsException() throws Exception {
        when(userSurahProgressDao.getUserSurahProgress(1, 114))
                .thenThrow(new FailedToGetSurahProgress());

        assertThrows(FailedToGetSurahProgress.class, () ->
                userSurahProgressService.getUserSurahProgress(1, 114));
    }

    @Test
    void getAllSurahProgressByUser_success() throws Exception {
        List<UserSurahProgress> mockList = Arrays.asList(
                new UserSurahProgress(201, 1, 1, true, "2024-03-01 09:00:00"),
                new UserSurahProgress(202, 1, 2, false, "2024-04-10 12:00:00")
        );

        when(userSurahProgressDao.getAllSurahProgressByUser(1)).thenReturn(mockList);

        List<UserSurahProgress> result = userSurahProgressService.getAllSurahProgressByUser(1);

        assertEquals(2, result.size());
        assertEquals(201, result.get(0).getProgressId());
        assertTrue(result.get(0).isMemorized());
        assertEquals(202, result.get(1).getProgressId());
        assertFalse(result.get(1).isMemorized());
    }

    @Test
    void getAllSurahProgressByUser_throwsException() throws Exception {
        when(userSurahProgressDao.getAllSurahProgressByUser(1))
                .thenThrow(new FailedToGetSurahProgress());

        assertThrows(FailedToGetSurahProgress.class, () ->
                userSurahProgressService.getAllSurahProgressByUser(1));
    }
}
