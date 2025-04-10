package com.fyp.db;

import com.fyp.cli.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private final DatabaseConnector databaseConnector;

    public UserDao(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    // ✅ Search Users by Email or Name (Excluding Current User)
    public List<User> searchUsers(String query, int currentUserId) throws SQLException {
        List<User> users = new ArrayList<>();
        Connection c = databaseConnector.getConnection();

        String sql = "SELECT UserID, Email FROM Users " +
                "WHERE (Email LIKE ?) " +
                "AND UserID != ?";

        PreparedStatement ps = c.prepareStatement(sql);
        ps.setString(1, "%" + query + "%"); // Match part of email
        ps.setInt(2, currentUserId); // Exclude current user

        ResultSet rs = ps.executeQuery();
        System.out.printf(rs.toString());
        while (rs.next()) {
            User user = new User(rs.getInt("UserID"), rs.getString("Email"), null);
            users.add(user);
        }
        return users;
    }

    // ✅ Get User by ID
    public User getUserById(int userId) throws SQLException {
        Connection c = databaseConnector.getConnection();
        String sql = "SELECT UserID, Email FROM Users WHERE UserID = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("UserID"),
                        rs.getString("Email"),
                        null
                );
            }
        }

        return null; // Return null if user not found
    }
}

