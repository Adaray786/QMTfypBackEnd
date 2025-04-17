package com.fyp.db;

import com.fyp.cli.ActiveChallenge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ActiveChallengeDaoTest {

    private DatabaseConnector databaseConnector = Mockito.mock(DatabaseConnector.class);
    private Connection connection = Mockito.mock(Connection.class);
    private PreparedStatement statement = Mockito.mock(PreparedStatement.class);
    private ResultSet resultSet = Mockito.mock(ResultSet.class);

    private ActiveChallengeDao activeChallengeDao = new ActiveChallengeDao(databaseConnector);

    @BeforeEach
    void setUp() throws SQLException {
        connection = mock(Connection.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);

        // 👇 Very important
        when(connection.isClosed()).thenReturn(false);

        // Set it directly
        DatabaseConnector.setConn(connection);  // ✅ Set the connection manually

        activeChallengeDao = new ActiveChallengeDao(new DatabaseConnector()); // this will use static conn

        // Mock behavior
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
    }



    @Test
    void getActiveChallenges_shouldReturnListOfActiveChallenges() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true, false); // one record
        when(resultSet.getInt("ChallengeID")).thenReturn(1);
        when(resultSet.getInt("User1ID")).thenReturn(10);
        when(resultSet.getInt("User2ID")).thenReturn(20);
        when(resultSet.getInt("SurahID")).thenReturn(114);
        when(resultSet.getString("Started_At")).thenReturn("2024-04-01 10:00:00");
        when(resultSet.getString("OtherUserEmail")).thenReturn("other@example.com");

        List<ActiveChallenge> result = activeChallengeDao.getActiveChallenges(10);

        assertEquals(1, result.size());
        ActiveChallenge challenge = result.get(0);
        assertEquals(1, challenge.getChallengeId());
        assertEquals(10, challenge.getUser1Id());
        assertEquals(20, challenge.getUser2Id());
        assertEquals("other@example.com", challenge.getOtherUserEmail());
    }

    @Test
    void getActiveChallengeById_shouldReturnChallenge() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("ChallengeID")).thenReturn(2);
        when(resultSet.getInt("User1ID")).thenReturn(5);
        when(resultSet.getInt("User2ID")).thenReturn(6);
        when(resultSet.getInt("SurahID")).thenReturn(2);
        when(resultSet.getString("Started_At")).thenReturn("2024-01-01 09:00:00");

        ActiveChallenge result = activeChallengeDao.getActiveChallengeById(2);

        assertNotNull(result);
        assertEquals(2, result.getChallengeId());
        assertEquals(5, result.getUser1Id());
    }

    @Test
    void getActiveChallengeBySurahAndUser_shouldReturnCorrectChallenge() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("ChallengeID")).thenReturn(3);
        when(resultSet.getInt("User1ID")).thenReturn(11);
        when(resultSet.getInt("User2ID")).thenReturn(12);
        when(resultSet.getInt("SurahID")).thenReturn(3);
        when(resultSet.getString("Started_At")).thenReturn("2024-02-02 10:00:00");
        when(resultSet.getString("OtherUserEmail")).thenReturn("friend@example.com");

        ActiveChallenge result = activeChallengeDao.getActiveChallengeBySurahAndUser(3, 11);

        assertNotNull(result);
        assertEquals(3, result.getChallengeId());
        assertEquals("friend@example.com", result.getOtherUserEmail());
    }

    @Test
    void removeActiveChallenge_shouldExecuteDeleteStatement() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);

        assertDoesNotThrow(() -> activeChallengeDao.removeActiveChallenge(100));

        verify(statement, times(1)).setInt(1, 100);
        verify(statement, times(1)).executeUpdate();
    }
}
