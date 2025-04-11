package com.fyp.db;

import com.fyp.cli.UserSurahProgress;
import com.fyp.client.FailedToGetSurahProgress;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserSurahProgressDao {

    private DatabaseConnector databaseConnector;

    public UserSurahProgressDao (DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    // Create or Update Surah Progress
    public void createOrUpdateSurahProgress(int userId, int surahId, boolean isMemorized) throws SQLException {
        String query =
            "INSERT INTO User_Surah_Progress (UserID, SurahID, Is_Memorized) " +
            "VALUES (?, ?, ?) " +
            "ON DUPLICATE KEY UPDATE Is_Memorized = ?;"
        ;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, surahId);
            pstmt.setBoolean(3, isMemorized);
            pstmt.setBoolean(4, isMemorized);
            pstmt.executeUpdate();
        }
    }

    // Fetch Surah progress by user and surah
    public UserSurahProgress getUserSurahProgress(int userId, int surahId) throws FailedToGetSurahProgress {
        String query = "SELECT * FROM User_Surah_Progress WHERE UserID = ? AND SurahID = ?";
        UserSurahProgress progress = null;

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, surahId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    progress = new UserSurahProgress(
                            rs.getInt("ProgressID"),
                            rs.getInt("UserID"),
                            rs.getInt("SurahID"),
                            rs.getBoolean("Is_Memorized"),
                            rs.getString("Last_Revised_At")
                    );
                }
            }
        } catch (SQLException e) {
            throw new FailedToGetSurahProgress();
        }
        return progress;
    }

    // Fetch all Surah progress records for a user
    public List<UserSurahProgress> getAllSurahProgressByUser(int userId) throws FailedToGetSurahProgress {
        List<UserSurahProgress> progressList = new ArrayList<>();
        String query = "SELECT * FROM User_Surah_Progress WHERE UserID = ?";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    progressList.add(new UserSurahProgress(
                            rs.getInt("ProgressID"),
                            rs.getInt("UserID"),
                            rs.getInt("SurahID"),
                            rs.getBoolean("Is_Memorized"),
                            rs.getString("Last_Revised_At")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new FailedToGetSurahProgress();
        }
        return progressList;
    }

    public void updateUserSurahProgress(int userId, int surahId, boolean isMemorized) throws SQLException {
        String query =
                "INSERT INTO User_Surah_Progress (UserID, SurahID, Is_Memorized, Last_Revised_At) " +
                        "VALUES (?, ?, ?, CURRENT_TIMESTAMP) " +
                        "ON DUPLICATE KEY UPDATE Is_Memorized = ?, Last_Revised_At = CURRENT_TIMESTAMP;";
         
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, userId);
            pstmt.setInt(2, surahId);
            pstmt.setBoolean(3, isMemorized);
            pstmt.setBoolean(4, isMemorized);

            pstmt.executeUpdate();
        }
    }

    public boolean hasUserMemorizedSurah(int userId, int surahId) throws SQLException {
        String query = "SELECT Is_Memorized FROM User_Surah_Progress WHERE UserID = ? AND SurahID = ?";
        
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, userId);
            pstmt.setInt(2, surahId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("Is_Memorized");
                }
            }
        }
        return false; // If no record found, assume not memorized
    }

    public void updateLastRevised(int userId, int surahId) throws SQLException {
        String query = "UPDATE User_Surah_Progress " +
                      "SET Last_Revised_At = CURRENT_TIMESTAMP " +
                      "WHERE UserID = ? AND SurahID = ?";
        
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, userId);
            pstmt.setInt(2, surahId);
            pstmt.executeUpdate();
        }
    }
}
