package com.fyp.db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserScoreDaoTest {

    private Connection connection = Mockito.mock(Connection.class);
    private PreparedStatement statement = Mockito.mock(PreparedStatement.class);
    private ResultSet resultSet = Mockito.mock(ResultSet.class);

    private UserScoreDao userScoreDao;

    @BeforeEach
    void setUp() throws SQLException {
        DatabaseConnector.setConn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);

        userScoreDao = new UserScoreDao(new DatabaseConnector());
    }

    @Test
    void getUserScore_ShouldReturnScore_WhenUserExists() throws SQLException {
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("Total_Score")).thenReturn(85);

        int score = userScoreDao.getUserScore(1);

        assertEquals(85, score);
        verify(statement).setInt(1, 1);
        verify(statement).executeQuery();
    }

    @Test
    void getUserScore_ShouldReturnZero_WhenUserNotFound() throws SQLException {
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        int score = userScoreDao.getUserScore(999);

        assertEquals(0, score);
    }
}
