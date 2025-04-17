package com.fyp.db;

import com.fyp.cli.CompletedChallenge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class CompletedChallengeDaoTest {

    private Connection connection = Mockito.mock(Connection.class);
    private PreparedStatement statement = Mockito.mock(PreparedStatement.class);
    private ResultSet resultSet = Mockito.mock(ResultSet.class);

    private CompletedChallengeDao completedChallengeDao;

    @BeforeEach
    void setUp() throws SQLException {
        DatabaseConnector.setConn(connection); // Static DB setup
        when(connection.prepareStatement(anyString())).thenReturn(statement);

        completedChallengeDao = new CompletedChallengeDao(new DatabaseConnector());
    }

    @Test
    void getCompletedChallenges_ShouldReturnListOfChallenges() throws SQLException {
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);

        when(resultSet.getInt("CompletedChallengeID")).thenReturn(1);
        when(resultSet.getInt("ChallengerID")).thenReturn(2);
        when(resultSet.getInt("ChallengedID")).thenReturn(3);
        when(resultSet.getInt("SurahID")).thenReturn(114);
        when(resultSet.getInt("WinnerID")).thenReturn(2);
        when(resultSet.getString("Started_At")).thenReturn("2024-04-01 09:00:00");
        when(resultSet.getString("Completed_At")).thenReturn("2024-04-01 10:00:00");
        when(resultSet.getString("OtherUserEmail")).thenReturn("other@example.com");

        List<CompletedChallenge> results = completedChallengeDao.getCompletedChallenges(2);

        assertEquals(1, results.size());

        CompletedChallenge challenge = results.get(0);
        assertEquals(1, challenge.getCompletedChallengeId());
        assertEquals(2, challenge.getChallengerId());
        assertEquals(3, challenge.getChallengedId());
        assertEquals(114, challenge.getSurahId());
        assertEquals(2, challenge.getWinnerId());
        assertEquals("2024-04-01 09:00:00", challenge.getStartedAt());
        assertEquals("2024-04-01 10:00:00", challenge.getCompletedAt());
        assertEquals("other@example.com", challenge.getOtherUserEmail());
    }

    @Test
    void getCompletedChallenges_ShouldReturnEmptyList_WhenNoMatchesFound() throws SQLException {
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        List<CompletedChallenge> results = completedChallengeDao.getCompletedChallenges(999);
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void createCompletedChallenge_ShouldInsertNewChallenge() throws SQLException {
        when(statement.executeUpdate()).thenReturn(1);

        completedChallengeDao.createCompletedChallenge(
                1, 2, 114, 1, "2024-04-01 09:00:00");

        verify(statement).setInt(1, 1);
        verify(statement).setInt(2, 2);
        verify(statement).setInt(3, 114);
        verify(statement).setInt(4, 1);
        verify(statement).setString(5, "2024-04-01 09:00:00");
        verify(statement).executeUpdate();
    }
}
