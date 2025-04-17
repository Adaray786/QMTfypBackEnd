package com.fyp.api;

import com.fyp.cli.User;
import com.fyp.client.FailedToSearchUsersException;
import com.fyp.client.FailedToGetUserByIdException;
import com.fyp.db.UserDao;

import java.sql.SQLException;
import java.util.List;

public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    // ✅ Search Users by Query
    public List<User> searchUsers(String query, int currentUserId) throws FailedToSearchUsersException {
        try {
            return userDao.searchUsers(query, currentUserId);
        } catch (SQLException e) {
            throw new FailedToSearchUsersException("Failed to search users with query: " + query, e);
        }
    }

    // ✅ Get User by ID
    public User getUserById(int userId) throws FailedToGetUserByIdException {
        try {
            return userDao.getUserById(userId);
        } catch (SQLException e) {
            throw new FailedToGetUserByIdException("Failed to retrieve user with ID: " + userId, e);
        }
    }
}
