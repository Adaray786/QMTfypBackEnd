package com.fyp.db;

import com.fyp.cli.UserSurahProgress;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecommendationDao {

    private DatabaseConnector databaseConnector;

    public RecommendationDao(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    // Get surahs that need revision (memorized surahs not revised recently)
    public List<UserSurahProgress> getSurahsNeedingRevision(int userId) {
        List<UserSurahProgress> surahs = new ArrayList<>();
        String query = "WITH RankedSurahs AS (" +
                      "  SELECT DISTINCT " +
                      "    usp.ProgressID, " +
                      "    usp.UserID, " +
                      "    usp.SurahID, " +
                      "    usp.Is_Memorized, " +
                      "    usp.Last_Revised_At, " +
                      "    s.Surah_Name_English, " +
                      "    ROW_NUMBER() OVER (PARTITION BY usp.SurahID ORDER BY usp.Last_Revised_At ASC) as rn " +
                      "  FROM User_Surah_Progress usp " +
                      "  JOIN Surahs s ON usp.SurahID = s.SurahID " +
                      "  WHERE usp.UserID = ? " +
                      "    AND usp.Is_Memorized = true " +
                      "    AND (usp.Last_Revised_At IS NULL OR " +
                      "         TIMESTAMPDIFF(DAY, usp.Last_Revised_At, CURRENT_TIMESTAMP) >= 7) " +
                      ") " +
                      "SELECT * FROM RankedSurahs " +
                      "WHERE rn = 1 " +
                      "ORDER BY " +
                      "  CASE WHEN Last_Revised_At IS NULL THEN 1 ELSE 2 END, " +
                      "  Last_Revised_At ASC " +
                      "LIMIT 5";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    surahs.add(new UserSurahProgress(
                        rs.getInt("ProgressID"),
                        rs.getInt("UserID"),
                        rs.getInt("SurahID"),
                        rs.getBoolean("Is_Memorized"),
                        rs.getString("Last_Revised_At")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching surahs needing revision: " + e.getMessage());
        }
        return surahs;
    }

    // Get new surahs to memorize (not yet memorized, ordered by ayah count)
    public List<Integer> getNewSurahsToMemorize(int userId) {
        List<Integer> surahIds = new ArrayList<>();
        String query = "WITH UnmemorizedSurahs AS (" +
                      "  SELECT DISTINCT s.SurahID " +
                      "  FROM Surahs s " +
                      "  LEFT JOIN User_Surah_Progress usp ON s.SurahID = usp.SurahID AND usp.UserID = ? " +
                      "  WHERE (usp.SurahID IS NULL OR usp.Is_Memorized = false) " +
                      "    AND s.SurahID NOT IN ( " +
                      "      SELECT SurahID FROM User_Surah_Progress " +
                      "      WHERE UserID = ? AND Is_Memorized = true " +
                      "    ) " +
                      ") " +
                      "SELECT SurahID FROM UnmemorizedSurahs " +
                      "ORDER BY (SELECT Total_Ayahs FROM Surahs WHERE SurahID = UnmemorizedSurahs.SurahID) ASC " +
                      "LIMIT 5";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, userId);
            pstmt.setInt(2, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    surahIds.add(rs.getInt("SurahID"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching new surahs to memorize: " + e.getMessage());
        }
        return surahIds;
    }

    // Update last revised timestamp for a surah
    public void updateLastRevised(int userId, int surahId) {
        String query = "UPDATE User_Surah_Progress SET Last_Revised_At = CURRENT_TIMESTAMP WHERE UserID = ? AND SurahID = ?";
        
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, userId);
            pstmt.setInt(2, surahId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating last revised timestamp: " + e.getMessage());
        }
    }
} 