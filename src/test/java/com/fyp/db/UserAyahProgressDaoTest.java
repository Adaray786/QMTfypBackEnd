package com.fyp.db;

import com.fyp.cli.UserAyahProgress;
import com.fyp.client.FailedToCheckAyahProgressException;
import com.fyp.client.FailedToGetAyahProgress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserAyahProgressDaoTest {

    private Connection connection = Mockito.mock(Connection.class);
    private PreparedStatement statement = Mockito.mock(PreparedStatement.class);
    private ResultSet resultSet = Mockito.mock(ResultSet.class);

    private UserAyahProgressDao dao;

    @BeforeEach
    void setUp() throws SQLException {
        DatabaseConnector.setConn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        dao = new UserAyahProgressDao(new DatabaseConnector());
    }

    @Test
    void updateAyahProgress_ShouldUpdateProgressAndScore_WhenMemorized() throws SQLException {
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("Word_Count")).thenReturn(5);

        dao.updateAyahProgress(1, 2, true);

        verify(statement, atLeastOnce()).executeUpdate();
        verify(statement, atLeastOnce()).setInt(anyInt(), anyInt());
    }

    @Test
    void updateAyahProgress_ShouldOnlyUpdateProgress_WhenNotMemorized() throws SQLException {
        dao.updateAyahProgress(1, 2, false);

        verify(statement, times(1)).setBoolean(1, false);
        verify(statement, times(1)).executeUpdate();
    }

    @Test
    void getUserAyahProgress_ShouldReturnProgress_WhenFound() throws Exception {
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("ProgressID")).thenReturn(100);
        when(resultSet.getInt("UserID")).thenReturn(1);
        when(resultSet.getInt("AyahID")).thenReturn(5);
        when(resultSet.getBoolean("Is_Memorized")).thenReturn(true);

        UserAyahProgress progress = dao.getUserAyahProgress(1, 5);

        assertNotNull(progress);
        assertEquals(100, progress.getProgressId());
        assertEquals(5, progress.getAyahId());
        assertTrue(progress.isMemorized());
    }

    @Test
    void getUserAyahProgress_ShouldReturnNull_WhenNotFound() throws Exception {
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        UserAyahProgress progress = dao.getUserAyahProgress(1, 99);
        assertNull(progress);
    }

    @Test
    void getAllAyahProgressByUser_ShouldReturnListOfProgress() throws Exception {
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("ProgressID")).thenReturn(1);
        when(resultSet.getInt("UserID")).thenReturn(10);
        when(resultSet.getInt("AyahID")).thenReturn(100);
        when(resultSet.getBoolean("Is_Memorized")).thenReturn(true);

        List<UserAyahProgress> result = dao.getAllAyahProgressByUser(10);

        assertEquals(1, result.size());
        assertEquals(100, result.get(0).getAyahId());
    }

    @Test
    void getAyahProgressBySurahAndUser_ShouldReturnProgressList() throws Exception, FailedToCheckAyahProgressException {
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);

        when(resultSet.getInt("ProgressID")).thenReturn(1);
        when(resultSet.getInt("UserID")).thenReturn(10);
        when(resultSet.getInt("AyahID")).thenReturn(5);
        when(resultSet.getBoolean("Is_Memorized")).thenReturn(true);

        List<UserAyahProgress> result = dao.getAyahProgressBySurahAndUser(10, 2);

        assertEquals(1, result.size());
        assertEquals(5, result.get(0).getAyahId());
        assertTrue(result.get(0).isMemorized());
    }

    @Test
    void getAyahProgressBySurahAndUser_ShouldThrowException_WhenSQLExceptionOccurs() throws SQLException {
        when(statement.executeQuery()).thenThrow(SQLException.class);
        assertThrows(FailedToCheckAyahProgressException.class,
                () -> dao.getAyahProgressBySurahAndUser(1, 1));
    }
}
