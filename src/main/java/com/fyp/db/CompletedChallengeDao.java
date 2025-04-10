package com.fyp.db;

import com.fyp.cli.CompletedChallenge;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompletedChallengeDao {
    private final DatabaseConnector databaseConnector;

    public CompletedChallengeDao(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    public List<CompletedChallenge> getCompletedChallenges(int userId) throws SQLException {
        List<CompletedChallenge> challenges = new ArrayList<>();
        String query = "SELECT cc.*, " +
                      "CASE " +
                      "    WHEN cc.ChallengerID = ? THEN u2.Email " +
                      "    ELSE u1.Email " +
                      "END as OtherUserEmail " +
                      "FROM Completed_Challenges cc " +
                      "JOIN Users u1 ON cc.ChallengerID = u1.UserID " +
                      "JOIN Users u2 ON cc.ChallengedID = u2.UserID " +
                      "WHERE cc.ChallengerID = ? OR cc.ChallengedID = ?";
        
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ps.setInt(3, userId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                challenges.add(new CompletedChallenge(
                    rs.getInt("CompletedChallengeID"),
                    rs.getInt("ChallengerID"),
                    rs.getInt("ChallengedID"),
                    rs.getInt("SurahID"),
                    rs.getInt("WinnerID"),
                    rs.getString("Started_At"),
                    rs.getString("Completed_At"),
                    rs.getString("OtherUserEmail")
                ));
            }
        }
        
        return challenges;
    }

    public void createCompletedChallenge(int challengerId, int challengedId, int surahId, 
                                      int winnerId, String startedAt) throws SQLException {
        String query = "INSERT INTO Completed_Challenges " +
                      "(ChallengerID, ChallengedID, SurahID, WinnerID, Started_At) " +
                      "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, challengerId);
            ps.setInt(2, challengedId);
            ps.setInt(3, surahId);
            ps.setInt(4, winnerId);
            ps.setString(5, startedAt);
            ps.executeUpdate();
        }
    }
} 