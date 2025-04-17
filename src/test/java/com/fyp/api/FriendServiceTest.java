package com.fyp.api;

import com.fyp.cli.Friend;
import com.fyp.cli.FriendRequest;
import com.fyp.client.FriendRequestAlreadyExistsException;
import com.fyp.client.FriendRequestNotFoundException;
import com.fyp.client.FriendNotFoundException;
import com.fyp.db.FriendDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FriendServiceTest {

    private FriendDao friendDao;
    private FriendService friendService;

    @BeforeEach
    void setUp() {
        friendDao = mock(FriendDao.class);
        friendService = new FriendService(friendDao);
    }

    @Test
    void sendFriendRequest_success() throws Exception {
        friendService.sendFriendRequest(1, 2);
        verify(friendDao).sendFriendRequest(1, 2);
    }

    @Test
    void sendFriendRequest_duplicateThrowsException() throws Exception {
        doThrow(new SQLException("duplicate")).when(friendDao).sendFriendRequest(1, 2);

        assertThrows(FriendRequestAlreadyExistsException.class, () -> {
            friendService.sendFriendRequest(1, 2);
        });

        verify(friendDao).sendFriendRequest(1, 2);
    }

    @Test
    void sendFriendRequest_otherSqlExceptionRethrown() throws Exception {
        doThrow(new SQLException("some other SQL issue")).when(friendDao).sendFriendRequest(1, 2);

        assertThrows(SQLException.class, () -> {
            friendService.sendFriendRequest(1, 2);
        });
    }

    @Test
    void acceptFriendRequest_success() throws Exception {
        when(friendDao.acceptFriendRequest(10)).thenReturn(1);

        friendService.acceptFriendRequest(10);

        verify(friendDao).acceptFriendRequest(10);
    }

    @Test
    void acceptFriendRequest_notFound() throws Exception {
        when(friendDao.acceptFriendRequest(999)).thenReturn(0);

        assertThrows(FriendRequestNotFoundException.class, () -> {
            friendService.acceptFriendRequest(999);
        });
    }

    @Test
    void rejectFriendRequest_success() throws Exception {
        when(friendDao.rejectFriendRequest(10)).thenReturn(1);

        friendService.rejectFriendRequest(10);

        verify(friendDao).rejectFriendRequest(10);
    }

    @Test
    void rejectFriendRequest_notFound() throws Exception {
        when(friendDao.rejectFriendRequest(999)).thenReturn(0);

        assertThrows(FriendRequestNotFoundException.class, () -> {
            friendService.rejectFriendRequest(999);
        });
    }

    @Test
    void removeFriend_success() throws Exception {
        when(friendDao.removeFriend(1, 2)).thenReturn(1);

        friendService.removeFriend(1, 2);

        verify(friendDao).removeFriend(1, 2);
    }

    @Test
    void removeFriend_notFound() throws Exception {
        when(friendDao.removeFriend(1, 2)).thenReturn(0);

        assertThrows(FriendNotFoundException.class, () -> {
            friendService.removeFriend(1, 2);
        });
    }

    @Test
    void getFriendRequests_returnsList() throws Exception {
        FriendRequest req1 = new FriendRequest(101, 1, "Alice");
        FriendRequest req2 = new FriendRequest(102, 2, "Bob");

        List<FriendRequest> mockRequests = Arrays.asList(req1, req2);
        when(friendDao.getFriendRequests(1)).thenReturn(mockRequests);

        List<FriendRequest> result = friendService.getFriendRequests(1);

        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getSenderName());
        assertEquals(102, result.get(1).getRequestId());
        verify(friendDao).getFriendRequests(1);
    }


    @Test
    void getFriends_returnsList() throws Exception {
        Friend friend1 = new Friend(201, "Amina");
        Friend friend2 = new Friend(202, "Bilal");
        Friend friend3 = new Friend(203, "Fatima");

        List<Friend> mockFriends = Arrays.asList(friend1, friend2, friend3);
        when(friendDao.getFriends(1)).thenReturn(mockFriends);

        List<Friend> result = friendService.getFriends(1);

        assertEquals(3, result.size());
        assertEquals("Amina", result.get(0).getFriendName());
        assertEquals(202, result.get(1).getFriendId());
        assertEquals("Fatima", result.get(2).getFriendName());

        verify(friendDao).getFriends(1);
    }

}
