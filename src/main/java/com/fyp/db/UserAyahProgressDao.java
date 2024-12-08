package com.fyp.db;

import com.fyp.cli.UserAyahProgress;
import com.fyp.client.FailedToGetAyahProgress;
import com.fyp.client.FailedToInsertAyahProgress;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserAyahProgressDao {

    private final DatabaseConnector databaseConnector = new DatabaseConnector();

    // Create or Update Ayah Progress
    public void createOrUpdateAyahProgress(int userId, int ayahId, boolean isMemorized) throws FailedToInsertAyahProgress {
        String query =
            "INSERT INTO User_Ayah_Progress (UserID, AyahID, Is_Memorized) " +
            "VALUES (?, ?, ?) " +
            "ON DUPLICATE KEY UPDATE Is_Memorized = ?;"
        ;

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, ayahId);
            pstmt.setBoolean(3, isMemorized);
            pstmt.setBoolean(4, isMemorized);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new FailedToInsertAyahProgress();
        }
    }

    // Fetch Ayah progress by user and ayah
    public UserAyahProgress getUserAyahProgress(int userId, int ayahId) throws FailedToGetAyahProgress {
        String query = "SELECT * FROM User_Ayah_Progress WHERE UserID = ? AND AyahID = ?";
        UserAyahProgress progress = null;

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, ayahId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    progress = new UserAyahProgress(
                            rs.getInt("ProgressID"),
                            rs.getInt("UserID"),
                            rs.getInt("AyahID"),
                            rs.getBoolean("Is_Memorized")
                    );
                }
            }
        } catch (SQLException e) {
            throw new FailedToGetAyahProgress();
        }
        return progress;
    }

    // Fetch all Ayah progress records for a user
    public List<UserAyahProgress> getAllAyahProgressByUser(int userId) throws FailedToGetAyahProgress {
        List<UserAyahProgress> progressList = new ArrayList<>();
        String query = "SELECT * FROM User_Ayah_Progress WHERE UserID = ?";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    progressList.add(new UserAyahProgress(
                            rs.getInt("ProgressID"),
                            rs.getInt("UserID"),
                            rs.getInt("AyahID"),
                            rs.getBoolean("Is_Memorized")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new FailedToGetAyahProgress();
        }
        return progressList;
    }

    public boolean areAllAyahsMemorized(int userId, int surahId) throws SQLException {
        String query =
        "SELECT COUNT(*) AS UnmemorizedCount " +
        "FROM Ayahs a " +
        "LEFT JOIN User_Ayah_Progress uap " +
        "ON a.AyahID = uap.AyahID AND uap.UserID = ? " +
        "WHERE a.SurahID = ? AND (uap.Is_Memorized IS NULL OR uap.Is_Memorized = FALSE)";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, surahId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("UnmemorizedCount") == 0;
                }
            }
        }
        return false;
    }

}
