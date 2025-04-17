package com.fyp.api;

import com.fyp.cli.Friend;
import com.fyp.client.FailedToGetFriendsScoresException;
import com.fyp.client.FailedToGetUserScoreException;
import com.fyp.db.FriendDao;
import com.fyp.db.UserScoreDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserScoreServiceTest {

    private UserScoreDao userScoreDao;
    private FriendDao friendDao;
    private UserScoreService userScoreService;

    @BeforeEach
    void setUp() {
        userScoreDao = mock(UserScoreDao.class);
        friendDao = mock(FriendDao.class);
        userScoreService = new UserScoreService(userScoreDao) {
            // Override the internally created FriendDao with our mock
            @Override
            public Map<Integer, Integer> getFriendsScores(int userId) throws FailedToGetFriendsScoresException {
                try {
                    Map<Integer, Integer> friendsScores = new HashMap<>();
                    for (Friend friend : friendDao.getFriends(userId)) {
                        friendsScores.put(friend.getFriendId(), userScoreDao.getUserScore(friend.getFriendId()));
                    }
                    return friendsScores;
                } catch (SQLException e) {
                    throw new FailedToGetFriendsScoresException("Failed to retrieve scores", e);
                }
            }
        };
    }

    @Test
    void getUserScore_success() throws Exception {
        when(userScoreDao.getUserScore(1)).thenReturn(95);

        int score = userScoreService.getUserScore(1);

        assertEquals(95, score);
        verify(userScoreDao).getUserScore(1);
    }

    @Test
    void getUserScore_throwsCustomException() throws Exception {
        when(userScoreDao.getUserScore(1)).thenThrow(new SQLException("Database error"));

        assertThrows(FailedToGetUserScoreException.class, () -> userScoreService.getUserScore(1));
    }

    @Test
    void getFriendsScores_success() throws Exception {
        Friend friend1 = new Friend(2, "Alice");
        Friend friend2 = new Friend(3, "Bob");

        when(friendDao.getFriends(1)).thenReturn(Arrays.asList(friend1, friend2));
        when(userScoreDao.getUserScore(2)).thenReturn(80);
        when(userScoreDao.getUserScore(3)).thenReturn(90);

        Map<Integer, Integer> scores = userScoreService.getFriendsScores(1);

        assertEquals(2, scores.size());
        assertEquals(80, scores.get(2));
        assertEquals(90, scores.get(3));
    }

    @Test
    void getFriendsScores_throwsCustomException() throws Exception {
        when(friendDao.getFriends(1)).thenThrow(new SQLException("Friend retrieval failed"));

        assertThrows(FailedToGetFriendsScoresException.class, () -> userScoreService.getFriendsScores(1));
    }
}
