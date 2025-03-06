package com.fyp.api;

import com.fyp.cli.Friend;
import com.fyp.cli.FriendRequest;
import com.fyp.db.FriendDao;

import java.sql.SQLException;
import java.util.List;

public class FriendService {

    private final FriendDao friendDao;

    public FriendService(FriendDao friendDao) {
        this.friendDao = friendDao;
    }

    public void sendFriendRequest(int senderId, int receiverId) throws SQLException {
        friendDao.sendFriendRequest(senderId, receiverId);
    }

    public void acceptFriendRequest(int requestId) throws SQLException {
        friendDao.acceptFriendRequest(requestId);
    }

    public void rejectFriendRequest(int requestId) throws SQLException {
        friendDao.rejectFriendRequest(requestId);
    }

    public void removeFriend(int userId, int friendId) throws SQLException {
        friendDao.removeFriend(userId, friendId);
    }

    public List<FriendRequest> getFriendRequests(int userId) throws SQLException {
        return friendDao.getFriendRequests(userId);
    }

    public List<Friend> getFriends(int userId) throws SQLException {
        return friendDao.getFriends(userId);
    }
}
