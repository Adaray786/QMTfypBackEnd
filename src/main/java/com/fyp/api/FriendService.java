package com.fyp.api;

import com.fyp.cli.Friend;
import com.fyp.cli.FriendRequest;
import com.fyp.client.FriendRequestAlreadyExistsException;
import com.fyp.client.FriendRequestNotFoundException;
import com.fyp.client.FriendNotFoundException;
import com.fyp.db.FriendDao;

import java.sql.SQLException;
import java.util.List;

public class FriendService {

    private final FriendDao friendDao;

    public FriendService(FriendDao friendDao) {
        this.friendDao = friendDao;
    }

    public void sendFriendRequest(int senderId, int receiverId)
            throws SQLException, FriendRequestAlreadyExistsException {
        try {
            friendDao.sendFriendRequest(senderId, receiverId);
        } catch (SQLException e) {
            if (e.getMessage().contains("duplicate") || e.getMessage().contains("already exists")) {
                throw new FriendRequestAlreadyExistsException("Friend request already exists between these users.");
            }
            throw e; // rethrow if not a known condition
        }
    }

    public void acceptFriendRequest(int requestId)
            throws SQLException, FriendRequestNotFoundException {
        int affected = friendDao.acceptFriendRequest(requestId);
        if (affected == 0) {
            throw new FriendRequestNotFoundException("Friend request with ID " + requestId + " not found.");
        }
    }

    public void rejectFriendRequest(int requestId)
            throws SQLException, FriendRequestNotFoundException {
        int affected = friendDao.rejectFriendRequest(requestId);
        if (affected == 0) {
            throw new FriendRequestNotFoundException("Friend request with ID " + requestId + " not found.");
        }
    }

    public void removeFriend(int userId, int friendId)
            throws SQLException, FriendNotFoundException {
        int affected = friendDao.removeFriend(userId, friendId);
        if (affected == 0) {
            throw new FriendNotFoundException("Friendship between user " + userId + " and " + friendId + " not found.");
        }
    }

    public List<FriendRequest> getFriendRequests(int userId) throws SQLException {
        return friendDao.getFriendRequests(userId);
    }

    public List<Friend> getFriends(int userId) throws SQLException {
        return friendDao.getFriends(userId);
    }
}
