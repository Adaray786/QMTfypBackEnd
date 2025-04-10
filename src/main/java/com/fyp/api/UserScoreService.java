package com.fyp.api;

import com.fyp.db.UserScoreDao;
import com.fyp.db.FriendDao;
import com.fyp.cli.Friend;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class UserScoreService {

    private final UserScoreDao userScoreDao;
    private final FriendDao friendDao;

    public UserScoreService(UserScoreDao userScoreDao) {
        this.userScoreDao = userScoreDao;
        this.friendDao = new FriendDao(userScoreDao.getDatabaseConnector());
    }

    // ✅ Get User's Score
    public int getUserScore(int userId) throws SQLException {
        return userScoreDao.getUserScore(userId);
    }

    // ✅ Get Friends' Scores
    public Map<Integer, Integer> getFriendsScores(int userId) throws SQLException {
        Map<Integer, Integer> friendsScores = new HashMap<>();

        // Get list of friends
        List<Friend> friends = friendDao.getFriends(userId);

        // Get score for each friend
        for (Friend friend : friends) {
            int friendId = friend.getFriendId();
            int score = userScoreDao.getUserScore(friendId);
            friendsScores.put(friendId, score);
        }

        return friendsScores;
    }
}

