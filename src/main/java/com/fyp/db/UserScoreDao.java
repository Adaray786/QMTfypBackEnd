package com.fyp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserScoreDao {

    private final DatabaseConnector databaseConnector;

    public UserScoreDao(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    // ✅ Get User's Total Score
    public int getUserScore(int userId) throws SQLException {
        String query = "SELECT Total_Score FROM User_Scores WHERE UserID = ?";
        int score = 0; // Default score if not found

        try (Connection c = databaseConnector.getConnection();
             PreparedStatement ps = c.prepareStatement(query)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                score = rs.getInt("Total_Score");
            }

        } catch (SQLException e) {
            System.err.println("Error fetching user score: " + e.getMessage());
        }

        return score;
    }

}
