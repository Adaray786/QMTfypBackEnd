package com.fyp.api;

import com.fyp.db.UserScoreDao;
import java.sql.SQLException;

public class UserScoreService {

    private final UserScoreDao userScoreDao;

    public UserScoreService(UserScoreDao userScoreDao) {
        this.userScoreDao = userScoreDao;
    }

    // ✅ Get User's Score
    public int getUserScore(int userId) throws SQLException {
        return userScoreDao.getUserScore(userId);
    }
}
