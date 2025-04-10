package com.fyp.api;

import com.fyp.cli.User;
import com.fyp.db.UserDao;

import java.sql.SQLException;
import java.util.List;

public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    // ✅ Search Users by Query
    public List<User> searchUsers(String query, int currentUserId) throws SQLException {
        return userDao.searchUsers(query, currentUserId);
    }

    // ✅ Get User by ID
    public User getUserById(int userId) throws SQLException {
        return userDao.getUserById(userId);
    }
}

