package com.fyp.db;

import com.fyp.cli.ChallengeRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChallengeRequestDao {
    private final DatabaseConnector databaseConnector;

    public ChallengeRequestDao(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    public void sendChallengeRequest(int senderId, int receiverId, int surahId) throws SQLException {
        String query = "INSERT INTO Challenge_Requests (SenderID, ReceiverID, SurahID) VALUES (?, ?, ?)";
        
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, senderId);
            ps.setInt(2, receiverId);
            ps.setInt(3, surahId);
            ps.executeUpdate();
        }
    }

    public void acceptChallengeRequest(int requestId) throws SQLException {
        Connection conn = databaseConnector.getConnection();
        
        // Get request details
        String getRequestQuery = "SELECT * FROM Challenge_Requests WHERE RequestID = ?";
        try (PreparedStatement ps = conn.prepareStatement(getRequestQuery)) {
            ps.setInt(1, requestId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                int senderId = rs.getInt("SenderID");
                int receiverId = rs.getInt("ReceiverID");
                int surahId = rs.getInt("SurahID");
                
                // Update request status
                String updateQuery = "UPDATE Challenge_Requests SET Status = 'Accepted' WHERE RequestID = ?";
                try (PreparedStatement updatePs = conn.prepareStatement(updateQuery)) {
                    updatePs.setInt(1, requestId);
                    updatePs.executeUpdate();
                }
                
                // Create active challenge
                String insertChallengeQuery = "INSERT INTO Active_Challenges (User1ID, User2ID, SurahID) VALUES (?, ?, ?)";
                try (PreparedStatement insertPs = conn.prepareStatement(insertChallengeQuery)) {
                    insertPs.setInt(1, senderId);
                    insertPs.setInt(2, receiverId);
                    insertPs.setInt(3, surahId);
                    insertPs.executeUpdate();
                }
            }
        }
    }

    public void rejectChallengeRequest(int requestId) throws SQLException {
        String query = "UPDATE Challenge_Requests SET Status = 'Rejected' WHERE RequestID = ?";
        
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, requestId);
            ps.executeUpdate();
        }
    }

    public List<ChallengeRequest> getChallengeRequests(int userId) throws SQLException {
        List<ChallengeRequest> requests = new ArrayList<>();
        String query = "SELECT cr.*, u.Email as SenderEmail " +
                      "FROM Challenge_Requests cr " +
                      "JOIN Users u ON cr.SenderID = u.UserID " +
                      "WHERE cr.ReceiverID = ? AND cr.Status = 'Pending'";
        
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                requests.add(new ChallengeRequest(
                    rs.getInt("RequestID"),
                    rs.getInt("SenderID"),
                    rs.getInt("ReceiverID"),
                    rs.getInt("SurahID"),
                    rs.getString("Status"),
                    rs.getString("Sent_At"),
                    rs.getString("SenderEmail")
                ));
            }
        }
        
        return requests;
    }
} 