package com.fyp.resources;

import com.fyp.api.FriendService;
import com.fyp.cli.Friend;
import com.fyp.cli.FriendRequest;
import com.fyp.cli.User;
import com.fyp.client.FriendNotFoundException;
import com.fyp.client.FriendRequestAlreadyExistsException;
import com.fyp.client.FriendRequestNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FriendControllerTest {

    FriendService friendService = mock(FriendService.class);
    FriendController friendController;
    User mockUser;

    @BeforeEach
    void setUp() {
        friendController = new FriendController(friendService);
        mockUser = new User(1, "test@email.com", null);
    }

    @Test
    void sendFriendRequest_ShouldReturn200_WhenSuccessful() throws Exception {
        doNothing().when(friendService).sendFriendRequest(1, 2);
        Response response = friendController.sendFriendRequest(mockUser, 2);
        assertEquals(200, response.getStatus());
        assertEquals("Friend request sent to user 2", response.getEntity());
    }

    @Test
    void sendFriendRequest_ShouldReturn500_OnSQLException() throws Exception {
        doThrow(SQLException.class).when(friendService).sendFriendRequest(1, 2);
        Response response = friendController.sendFriendRequest(mockUser, 2);
        assertEquals(500, response.getStatus());
        assertEquals("Error sending friend request.", response.getEntity());
    }

    @Test
    void acceptFriendRequest_ShouldReturn200_WhenSuccessful() throws Exception {
        doNothing().when(friendService).acceptFriendRequest(1);
        Response response = friendController.acceptFriendRequest(mockUser, 1);
        assertEquals(200, response.getStatus());
        assertEquals("Friend request accepted.", response.getEntity());
    }

    @Test
    void acceptFriendRequest_ShouldReturn500_OnSQLException() throws Exception {
        doThrow(SQLException.class).when(friendService).acceptFriendRequest(1);
        Response response = friendController.acceptFriendRequest(mockUser, 1);
        assertEquals(500, response.getStatus());
        assertEquals("Error accepting friend request.", response.getEntity());
    }

    @Test
    void rejectFriendRequest_ShouldReturn200_WhenSuccessful() throws Exception {
        doNothing().when(friendService).rejectFriendRequest(5);
        Response response = friendController.rejectFriendRequest(mockUser, 5);
        assertEquals(200, response.getStatus());
        assertEquals("Friend request rejected.", response.getEntity());
    }

    @Test
    void rejectFriendRequest_ShouldReturn500_OnSQLException() throws Exception {
        doThrow(SQLException.class).when(friendService).rejectFriendRequest(5);
        Response response = friendController.rejectFriendRequest(mockUser, 5);
        assertEquals(500, response.getStatus());
        assertEquals("Error rejecting friend request.", response.getEntity());
    }

    @Test
    void removeFriend_ShouldReturn200_WhenSuccessful() throws Exception {
        doNothing().when(friendService).removeFriend(1, 2);
        Response response = friendController.removeFriend(mockUser, 2);
        assertEquals(200, response.getStatus());
        assertEquals("Friend removed successfully.", response.getEntity());
    }

    @Test
    void removeFriend_ShouldReturn500_OnSQLException() throws Exception {
        doThrow(SQLException.class).when(friendService).removeFriend(1, 2);
        Response response = friendController.removeFriend(mockUser, 2);
        assertEquals(500, response.getStatus());
        assertEquals("Error removing friend.", response.getEntity());
    }

    @Test
    void getFriendRequests_ShouldReturn200_WhenSuccessful() throws Exception {
        List<FriendRequest> requestList = new ArrayList<>();
        requestList.add(new FriendRequest(101, 2, "friend@example.com")); // ✅ Fixed

        when(friendService.getFriendRequests(1)).thenReturn(requestList);

        Response response = friendController.getFriendRequests(mockUser);
        assertEquals(200, response.getStatus());
        assertEquals(requestList, response.getEntity());
    }


    @Test
    void getFriendRequests_ShouldReturn500_OnSQLException() throws Exception {
        when(friendService.getFriendRequests(1)).thenThrow(SQLException.class);
        Response response = friendController.getFriendRequests(mockUser);
        assertEquals(500, response.getStatus());
        assertEquals("Error fetching friend requests.", response.getEntity());
    }

    @Test
    void getFriends_ShouldReturn200_WhenSuccessful() throws Exception {
        List<Friend> friends = new ArrayList<>();
        friends.add(new Friend(2, "friend@example.com"));
        when(friendService.getFriends(1)).thenReturn(friends);
        Response response = friendController.getFriends(mockUser);
        assertEquals(200, response.getStatus());
        assertEquals(friends, response.getEntity());
    }

    @Test
    void getFriends_ShouldReturn500_OnSQLException() throws Exception {
        when(friendService.getFriends(1)).thenThrow(SQLException.class);
        Response response = friendController.getFriends(mockUser);
        assertEquals(500, response.getStatus());
        assertEquals("Error fetching friends.", response.getEntity());
    }
}
