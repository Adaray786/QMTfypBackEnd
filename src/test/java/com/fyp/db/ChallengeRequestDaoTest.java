package com.fyp.db;

import com.fyp.cli.ChallengeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ChallengeRequestDaoTest {

    private Connection connection = Mockito.mock(Connection.class);
    private PreparedStatement statement = Mockito.mock(PreparedStatement.class);
    private ResultSet resultSet = Mockito.mock(ResultSet.class);

    private ChallengeRequestDao challengeRequestDao;

    @BeforeEach
    void setUp() throws SQLException {
        DatabaseConnector.setConn(connection); // ✅ Using static conn pattern
        when(connection.prepareStatement(anyString())).thenReturn(statement);

        challengeRequestDao = new ChallengeRequestDao(new DatabaseConnector());
    }

    @Test
    void sendChallengeRequest_ShouldInsertIntoDatabase() throws SQLException {
        when(statement.executeUpdate()).thenReturn(1);

        challengeRequestDao.sendChallengeRequest(1, 2, 114);

        verify(statement).setInt(1, 1);
        verify(statement).setInt(2, 2);
        verify(statement).setInt(3, 114);
        verify(statement).executeUpdate();
    }

    @Test
    void acceptChallengeRequest_ShouldUpdateStatusAndInsertActiveChallenge() throws SQLException {
        int requestId = 10;

        // Mock SELECT from Challenge_Requests
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("SenderID")).thenReturn(1);
        when(resultSet.getInt("ReceiverID")).thenReturn(2);
        when(resultSet.getInt("SurahID")).thenReturn(3);

        challengeRequestDao.acceptChallengeRequest(requestId);

        verify(statement, atLeastOnce()).executeUpdate(); // One for update, one for insert
    }

    @Test
    void rejectChallengeRequest_ShouldUpdateRequestStatus() throws SQLException {
        when(statement.executeUpdate()).thenReturn(1);

        challengeRequestDao.rejectChallengeRequest(22);

        verify(statement).setInt(1, 22);
        verify(statement).executeUpdate();
    }

    @Test
    void getChallengeRequests_ShouldReturnListOfRequests() throws SQLException {
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);

        when(resultSet.getInt("RequestID")).thenReturn(1);
        when(resultSet.getInt("SenderID")).thenReturn(2);
        when(resultSet.getInt("ReceiverID")).thenReturn(3);
        when(resultSet.getInt("SurahID")).thenReturn(114);
        when(resultSet.getString("Status")).thenReturn("Pending");
        when(resultSet.getString("Sent_At")).thenReturn("2024-01-01 10:00:00");
        when(resultSet.getString("SenderEmail")).thenReturn("user@example.com");

        List<ChallengeRequest> requests = challengeRequestDao.getChallengeRequests(3);

        assertEquals(1, requests.size());
        ChallengeRequest request = requests.get(0);
        assertEquals(1, request.getRequestId());
        assertEquals(2, request.getSenderId());
        assertEquals(3, request.getReceiverId());
        assertEquals("Pending", request.getStatus());
        assertEquals("user@example.com", request.getSenderEmail());
    }

    @Test
    void getChallengeRequests_ShouldReturnEmptyList_WhenNoRequestsFound() throws SQLException {
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        List<ChallengeRequest> requests = challengeRequestDao.getChallengeRequests(99);

        assertNotNull(requests);
        assertTrue(requests.isEmpty());
    }
}
