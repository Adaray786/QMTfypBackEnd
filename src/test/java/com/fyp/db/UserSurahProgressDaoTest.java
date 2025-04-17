package com.fyp.db;

import com.fyp.cli.UserSurahProgress;
import com.fyp.client.FailedToGetSurahProgress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserSurahProgressDaoTest {

    private Connection connection = mock(Connection.class);
    private PreparedStatement statement = mock(PreparedStatement.class);
    private ResultSet resultSet = mock(ResultSet.class);

    private UserSurahProgressDao dao;

    @BeforeEach
    void setUp() throws SQLException {
        DatabaseConnector.setConn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        dao = new UserSurahProgressDao(new DatabaseConnector());
    }

    @Test
    void createOrUpdateSurahProgress_ShouldExecuteUpdate() throws SQLException {
        when(statement.executeUpdate()).thenReturn(1);

        dao.createOrUpdateSurahProgress(1, 2);

        verify(statement).setInt(1, 1);
        verify(statement).setInt(2, 2);
        verify(statement).executeUpdate();
    }

    @Test
    void getUserSurahProgress_ShouldReturnProgress_WhenFound() throws Exception {
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("ProgressID")).thenReturn(1);
        when(resultSet.getInt("UserID")).thenReturn(10);
        when(resultSet.getInt("SurahID")).thenReturn(5);
        when(resultSet.getBoolean("Is_Memorized")).thenReturn(true);
        when(resultSet.getString("Last_Revised_At")).thenReturn("2024-04-01 10:00:00");

        UserSurahProgress result = dao.getUserSurahProgress(10, 5);

        assertNotNull(result);
        assertEquals(1, result.getProgressId());
        assertEquals(10, result.getUserId());
        assertEquals(5, result.getSurahId());
        assertTrue(result.isMemorized());
        assertEquals("2024-04-01 10:00:00", result.getLastRevisedAt());
    }

    @Test
    void getUserSurahProgress_ShouldReturnNull_WhenNotFound() throws Exception {
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        UserSurahProgress result = dao.getUserSurahProgress(10, 99);
        assertNull(result);
    }

    @Test
    void getAllSurahProgressByUser_ShouldReturnList() throws Exception {
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("ProgressID")).thenReturn(1);
        when(resultSet.getInt("UserID")).thenReturn(10);
        when(resultSet.getInt("SurahID")).thenReturn(5);
        when(resultSet.getBoolean("Is_Memorized")).thenReturn(true);
        when(resultSet.getString("Last_Revised_At")).thenReturn("2024-04-01 10:00:00");

        List<UserSurahProgress> result = dao.getAllSurahProgressByUser(10);

        assertEquals(1, result.size());
        assertEquals(5, result.get(0).getSurahId());
        assertTrue(result.get(0).isMemorized());
    }

    @Test
    void updateUserSurahProgress_ShouldInsertOrUpdate() throws SQLException {
        when(statement.executeUpdate()).thenReturn(1);

        dao.updateUserSurahProgress(1, 2, true);

        verify(statement).setInt(1, 1);
        verify(statement).setInt(2, 2);
        verify(statement).setBoolean(3, true);
        verify(statement).setBoolean(4, true);
        verify(statement).executeUpdate();
    }

    @Test
    void hasUserMemorizedSurah_ShouldReturnTrue_WhenFoundAndMemorized() throws SQLException {
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getBoolean("Is_Memorized")).thenReturn(true);

        boolean result = dao.hasUserMemorizedSurah(1, 114);

        assertTrue(result);
    }

    @Test
    void hasUserMemorizedSurah_ShouldReturnFalse_WhenNoRecordFound() throws SQLException {
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        boolean result = dao.hasUserMemorizedSurah(1, 114);

        assertFalse(result);
    }

    @Test
    void updateLastRevised_ShouldExecuteUpdate() throws SQLException {
        when(statement.executeUpdate()).thenReturn(1);

        dao.updateLastRevised(2, 3);

        verify(statement).setInt(1, 2);
        verify(statement).setInt(2, 3);
        verify(statement).executeUpdate();
    }
}
