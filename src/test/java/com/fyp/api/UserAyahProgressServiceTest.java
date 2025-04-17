package com.fyp.api;

import com.fyp.cli.UserAyahProgress;
import com.fyp.client.FailedToCheckAyahProgressException;
import com.fyp.client.FailedToGetAyahProgress;
import com.fyp.client.FailedToInsertAyahProgress;
import com.fyp.db.UserAyahProgressDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserAyahProgressServiceTest {

    private UserAyahProgressDao userAyahProgressDao;
    private UserAyahProgressService userAyahProgressService;

    @BeforeEach
    void setUp() {
        userAyahProgressDao = mock(UserAyahProgressDao.class);
        userAyahProgressService = new UserAyahProgressService(userAyahProgressDao);
    }

    @Test
    void getUserAyahProgress_success() throws Exception {
        UserAyahProgress progress = new UserAyahProgress(101, 1, 5, true); // Correct constructor
        when(userAyahProgressDao.getUserAyahProgress(1, 5)).thenReturn(progress);

        UserAyahProgress result = userAyahProgressService.getUserAyahProgress(1, 5);

        assertNotNull(result);
        assertEquals(1, result.getUserId());
        assertEquals(5, result.getAyahId());
        assertTrue(result.isMemorized());
    }

    @Test
    void getAllAyahProgressByUser_success() throws Exception {
        List<UserAyahProgress> progressList = Arrays.asList(
                new UserAyahProgress(201, 1, 1, true),
                new UserAyahProgress(202, 1, 2, false)
        );

        when(userAyahProgressDao.getAllAyahProgressByUser(1)).thenReturn(progressList);

        List<UserAyahProgress> result = userAyahProgressService.getAllAyahProgressByUser(1);

        assertEquals(2, result.size());
        assertTrue(result.get(0).isMemorized());
        assertFalse(result.get(1).isMemorized());
    }

    @Test
    void upsertUserAyahProgress_success() throws Exception {
        doNothing().when(userAyahProgressDao).updateAyahProgress(1, 10, true);

        assertDoesNotThrow(() ->
                userAyahProgressService.upsertUserAyahProgress(1, 10, true)
        );

        verify(userAyahProgressDao).updateAyahProgress(1, 10, true);
    }

    @Test
    void upsertUserAyahProgress_throwsSQLException() throws Exception {
        doThrow(new SQLException("DB error")).when(userAyahProgressDao).updateAyahProgress(1, 10, true);

        assertThrows(SQLException.class, () ->
                userAyahProgressService.upsertUserAyahProgress(1, 10, true)
        );
    }

    @Test
    void getAyahProgressBySurahAndUser_success() throws Exception, FailedToCheckAyahProgressException {
        List<UserAyahProgress> progressList = Arrays.asList(
                new UserAyahProgress(301, 2, 50, true),
                new UserAyahProgress(302, 2, 51, false)
        );

        when(userAyahProgressDao.getAyahProgressBySurahAndUser(2, 114)).thenReturn(progressList);

        List<UserAyahProgress> result = userAyahProgressService.getAyahProgressBySurahAndUser(2, 114);

        assertEquals(2, result.size());
        assertEquals(50, result.get(0).getAyahId());
        assertEquals(302, result.get(1).getProgressId());
    }

    @Test
    void getAyahProgressBySurahAndUser_throwsSQLException() throws Exception, FailedToCheckAyahProgressException {
        when(userAyahProgressDao.getAyahProgressBySurahAndUser(2, 114))
                .thenThrow(new SQLException("Something broke"));

        assertThrows(SQLException.class, () ->
                userAyahProgressService.getAyahProgressBySurahAndUser(2, 114));
    }
}
