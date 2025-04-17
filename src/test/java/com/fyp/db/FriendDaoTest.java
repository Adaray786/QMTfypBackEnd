package com.fyp.db;

import com.fyp.cli.Friend;
import com.fyp.cli.FriendRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class FriendDaoTest {

    private Connection connection = Mockito.mock(Connection.class);
    private PreparedStatement statement = Mockito.mock(PreparedStatement.class);
    private ResultSet resultSet = Mockito.mock(ResultSet.class);

    private FriendDao friendDao;

    @BeforeEach
    void setUp() throws SQLException {
        DatabaseConnector.setConn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);

        friendDao = new FriendDao(new DatabaseConnector());
    }

    @Test
    void sendFriendRequest_ShouldInsertFriendRequest() throws SQLException {
        when(statement.executeUpdate()).thenReturn(1);

        friendDao.sendFriendRequest(1, 2);

        verify(statement).setInt(1, 1);
        verify(statement).setInt(2, 2);
        verify(statement).executeUpdate();
    }

    @Test
    void acceptFriendRequest_ShouldInsertFriendAndUpdateRequest() throws SQLException {
        // Step 1: SELECT Sender and Receiver
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("SenderID")).thenReturn(1);
        when(resultSet.getInt("ReceiverID")).thenReturn(2);

        // Step 2: executeUpdate for both INSERT and UPDATE
        when(statement.executeUpdate()).thenReturn(1);

        int result = friendDao.acceptFriendRequest(5);

        assertEquals(1, result);
        verify(statement, atLeastOnce()).executeUpdate();
    }

    @Test
    void acceptFriendRequest_ShouldReturnZero_WhenNoRequestFound() throws SQLException {
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        int result = friendDao.acceptFriendRequest(10);

        assertEquals(0, result);
    }

    @Test
    void rejectFriendRequest_ShouldUpdateRequestStatus() throws SQLException {
        when(statement.executeUpdate()).thenReturn(1);

        int result = friendDao.rejectFriendRequest(3);

        assertEquals(1, result);
        verify(statement).setInt(1, 3);
        verify(statement).executeUpdate();
    }

    @Test
    void removeFriend_ShouldDeleteFriendRecord() throws SQLException {
        when(statement.executeUpdate()).thenReturn(1);

        int result = friendDao.removeFriend(1, 2);

        assertEquals(1, result);
        verify(statement).setInt(1, 1);
        verify(statement).setInt(2, 2);
        verify(statement).setInt(3, 2);
        verify(statement).setInt(4, 1);
        verify(statement).executeUpdate();
    }

    @Test
    void getFriendRequests_ShouldReturnPendingRequests() throws SQLException {
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("RequestID")).thenReturn(100);
        when(resultSet.getInt("SenderID")).thenReturn(5);
        when(resultSet.getString("SenderName")).thenReturn("test@example.com");

        List<FriendRequest> requests = friendDao.getFriendRequests(10);

        assertEquals(1, requests.size());
        FriendRequest request = requests.get(0);
        assertEquals(100, request.getRequestId());
        assertEquals(5, request.getSenderId());
        assertEquals("test@example.com", request.getSenderName());
    }

    @Test
    void getFriends_ShouldReturnFriendsList() throws SQLException {
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("FriendID")).thenReturn(9);
        when(resultSet.getString("FriendName")).thenReturn("friend@example.com");

        List<Friend> friends = friendDao.getFriends(7);

        assertEquals(1, friends.size());
        Friend friend = friends.get(0);
        assertEquals(9, friend.getFriendId());
        assertEquals("friend@example.com", friend.getFriendName());
    }
}
