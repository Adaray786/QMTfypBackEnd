package com.fyp.db;

import com.fyp.cli.ActiveChallenge;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActiveChallengeDao {
    private final DatabaseConnector databaseConnector;

    public ActiveChallengeDao(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    public List<ActiveChallenge> getActiveChallenges(int userId) throws SQLException {
        List<ActiveChallenge> challenges = new ArrayList<>();
        String query = "SELECT ac.*, " +
                      "CASE " +
                      "    WHEN ac.User1ID = ? THEN u2.Email " +
                      "    ELSE u1.Email " +
                      "END as OtherUserEmail " +
                      "FROM Active_Challenges ac " +
                      "JOIN Users u1 ON ac.User1ID = u1.UserID " +
                      "JOIN Users u2 ON ac.User2ID = u2.UserID " +
                      "WHERE ac.User1ID = ? OR ac.User2ID = ?";
        
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ps.setInt(3, userId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                challenges.add(new ActiveChallenge(
                    rs.getInt("ChallengeID"),
                    rs.getInt("User1ID"),
                    rs.getInt("User2ID"),
                    rs.getInt("SurahID"),
                    rs.getString("Started_At"),
                    rs.getString("OtherUserEmail")
                ));
            }
        }
        
        return challenges;
    }

    public void removeActiveChallenge(int challengeId) throws SQLException {
        String query = "DELETE FROM Active_Challenges WHERE ChallengeID = ?";
        
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, challengeId);
            ps.executeUpdate();
        }
    }

    public ActiveChallenge getActiveChallengeById(int challengeId) throws SQLException {
        String query = "SELECT * FROM Active_Challenges WHERE ChallengeID = ?";
        
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, challengeId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return new ActiveChallenge(
                    rs.getInt("ChallengeID"),
                    rs.getInt("User1ID"),
                    rs.getInt("User2ID"),
                    rs.getInt("SurahID"),
                    rs.getString("Started_At"),
                    null // otherUserEmail is not needed for this method
                );
            }
        }
        
        return null;
    }

    public ActiveChallenge getActiveChallengeBySurahAndUser(int surahId, int userId) throws SQLException {
        String query = "SELECT ac.*, " +
                      "CASE " +
                      "    WHEN ac.User1ID = ? THEN u2.Email " +
                      "    ELSE u1.Email " +
                      "END as OtherUserEmail " +
                      "FROM Active_Challenges ac " +
                      "JOIN Users u1 ON ac.User1ID = u1.UserID " +
                      "JOIN Users u2 ON ac.User2ID = u2.UserID " +
                      "WHERE ac.SurahID = ? AND (ac.User1ID = ? OR ac.User2ID = ?)";
        
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            ps.setInt(2, surahId);
            ps.setInt(3, userId);
            ps.setInt(4, userId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return new ActiveChallenge(
                    rs.getInt("ChallengeID"),
                    rs.getInt("User1ID"),
                    rs.getInt("User2ID"),
                    rs.getInt("SurahID"),
                    rs.getString("Started_At"),
                    rs.getString("OtherUserEmail")
                );
            }
        }
        
        return null;
    }
} 