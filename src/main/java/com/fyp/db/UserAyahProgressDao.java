package com.fyp.db;

import com.fyp.cli.UserAyahProgress;
import com.fyp.client.FailedToCheckAyahProgressException;
import com.fyp.client.FailedToGetAyahProgress;
import com.fyp.client.FailedToInsertAyahProgress;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserAyahProgressDao {

    private DatabaseConnector databaseConnector;

    public UserAyahProgressDao (DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    // Create or Update Ayah Progress
    public void updateAyahProgress(int userId, int ayahId, boolean isMemorized) throws SQLException {
        Connection c = databaseConnector.getConnection();

        // 1️⃣ Update Ayah Progress
        String updateProgressQuery = "UPDATE User_Ayah_Progress SET Is_Memorized = ? WHERE UserID = ? AND AyahID = ?";
        PreparedStatement psProgress = c.prepareStatement(updateProgressQuery);
        psProgress.setBoolean(1, isMemorized);
        psProgress.setInt(2, userId);
        psProgress.setInt(3, ayahId);
        psProgress.executeUpdate();

        // 2️⃣ Only Update Score IF Ayah is Marked as Memorized
        if (isMemorized) {
            String getWordCountQuery = "SELECT Word_Count FROM Ayahs WHERE AyahID = ?";
            PreparedStatement psWordCount = c.prepareStatement(getWordCountQuery);
            psWordCount.setInt(1, ayahId);
            ResultSet rs = psWordCount.executeQuery();

            if (rs.next()) {
                int wordCount = rs.getInt("Word_Count");

                // 3️⃣ Increment the User's Score
                String updateScoreQuery = "UPDATE User_Scores SET Total_Score = Total_Score + ? WHERE UserID = ?";
                PreparedStatement psUpdateScore = c.prepareStatement(updateScoreQuery);
                psUpdateScore.setInt(1, wordCount);
                psUpdateScore.setInt(2, userId);
                psUpdateScore.executeUpdate();
            }
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

    // Fetch all Ayah progress records for a user given a specific surah
    public List<UserAyahProgress> getAyahProgressBySurahAndUser(int userId, int surahId) throws SQLException, FailedToCheckAyahProgressException {
        List<UserAyahProgress> progressList = new ArrayList<>();
        String query =
        "SELECT a.AyahID, a.SurahID, uap.UserID, uap.ProgressID, uap.Is_Memorized " +
        "FROM Ayahs a " +
        "LEFT JOIN User_Ayah_Progress uap " +
        "ON a.AyahID = uap.AyahID AND uap.UserID = ? " +
        "WHERE a.SurahID = ?";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, surahId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    UserAyahProgress progress = new UserAyahProgress();
                    progress.setProgressId(rs.getInt("ProgressID"));
                    progress.setAyahId(rs.getInt("AyahID"));
                    progress.setUserId(rs.getInt("UserID"));
                    progress.setMemorized(rs.getBoolean("Is_Memorized"));
                    progressList.add(progress);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching User Ayah Progress: " + e.getMessage());
            throw new FailedToCheckAyahProgressException();
        }
        return progressList;
    }
}
