package com.fyp.api;

import com.fyp.db.UserScoreDao;
import com.fyp.db.FriendDao;
import com.fyp.cli.Friend;
import com.fyp.client.FailedToGetUserScoreException;
import com.fyp.client.FailedToGetFriendsScoresException;

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
    public int getUserScore(int userId) throws FailedToGetUserScoreException {
        try {
            return userScoreDao.getUserScore(userId);
        } catch (SQLException e) {
            throw new FailedToGetUserScoreException("Failed to retrieve score for user ID: " + userId, e);
        }
    }

    // ✅ Get Friends' Scores
    public Map<Integer, Integer> getFriendsScores(int userId) throws FailedToGetFriendsScoresException {
        Map<Integer, Integer> friendsScores = new HashMap<>();
        try {
            List<Friend> friends = friendDao.getFriends(userId);

            for (Friend friend : friends) {
                int friendId = friend.getFriendId();
                int score = userScoreDao.getUserScore(friendId);
                friendsScores.put(friendId, score);
            }

            return friendsScores;
        } catch (SQLException e) {
            throw new FailedToGetFriendsScoresException("Failed to retrieve scores for user's friends (user ID: " + userId + ")", e);
        }
    }
}
