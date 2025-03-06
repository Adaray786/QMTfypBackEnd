package com.fyp.db;

import com.fyp.cli.Friend;
import com.fyp.cli.FriendRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FriendDao {

    private DatabaseConnector databaseConnector;

    public FriendDao(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    // ✅ **Send a Friend Request**
    public void sendFriendRequest(int senderId, int receiverId) throws SQLException {
        Connection c = databaseConnector.getConnection();
        PreparedStatement ps = c.prepareStatement(
                "INSERT INTO Friend_Requests (SenderID, ReceiverID, Status) VALUES (?, ?, 'Pending')"
        );
        ps.setInt(1, senderId);
        ps.setInt(2, receiverId);
        ps.executeUpdate();
    }

    // ✅ **Accept a Friend Request**
    public void acceptFriendRequest(int requestId) throws SQLException {
        Connection c = databaseConnector.getConnection();

        // Get sender and receiver IDs from request
        PreparedStatement ps = c.prepareStatement(
                "SELECT SenderID, ReceiverID FROM Friend_Requests WHERE RequestID = ?"
        );
        ps.setInt(1, requestId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            int senderId = rs.getInt("SenderID");
            int receiverId = rs.getInt("ReceiverID");

            // Insert into Friends table
            PreparedStatement insertFriend = c.prepareStatement(
                    "INSERT INTO Friends (User1ID, User2ID) VALUES (?, ?)"
            );
            insertFriend.setInt(1, senderId);
            insertFriend.setInt(2, receiverId);
            insertFriend.executeUpdate();

            // Update Friend Request status to 'Accepted'
            PreparedStatement updateRequest = c.prepareStatement(
                    "UPDATE Friend_Requests SET Status = 'Accepted' WHERE RequestID = ?"
            );
            updateRequest.setInt(1, requestId);
            updateRequest.executeUpdate();
        }
    }

    // ✅ **Reject a Friend Request**
    public void rejectFriendRequest(int requestId) throws SQLException {
        Connection c = databaseConnector.getConnection();
        PreparedStatement ps = c.prepareStatement(
                "UPDATE Friend_Requests SET Status = 'Rejected' WHERE RequestID = ?"
        );
        ps.setInt(1, requestId);
        ps.executeUpdate();
    }

    // ✅ **Remove a Friend**
    public void removeFriend(int userId, int friendId) throws SQLException {
        Connection c = databaseConnector.getConnection();
        PreparedStatement ps = c.prepareStatement(
                "DELETE FROM Friends WHERE (User1ID = ? AND User2ID = ?) OR (User1ID = ? AND User2ID = ?)"
        );
        ps.setInt(1, userId);
        ps.setInt(2, friendId);
        ps.setInt(3, friendId);
        ps.setInt(4, userId);
        ps.executeUpdate();
    }

    // ✅ **Get Pending Friend Requests**
    public List<FriendRequest> getFriendRequests(int userId) throws SQLException {
        Connection c = databaseConnector.getConnection();

        // ✅ Fetch Sender Name along with Sender ID
        PreparedStatement ps = c.prepareStatement(
                "SELECT fr.RequestID, fr.SenderID, u.Email AS SenderName " +
                        "FROM Friend_Requests fr " +
                        "INNER JOIN Users u ON fr.SenderID = u.UserID " +
                        "WHERE fr.ReceiverID = ? AND fr.Status = 'Pending'"
        );

        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();

        List<FriendRequest> requests = new ArrayList<>();
        while (rs.next()) {
            requests.add(new FriendRequest(
                    rs.getInt("RequestID"),
                    rs.getInt("SenderID"),
                    rs.getString("SenderName")  // ✅ Include Sender Name
            ));
        }

        return requests;
    }


    public List<Friend> getFriends(int userId) throws SQLException {
        Connection c = databaseConnector.getConnection();

        // ✅ Fetch Friend Name along with Friend ID
        PreparedStatement ps = c.prepareStatement(
                "SELECT u.UserID AS FriendID, u.Email AS FriendName " +
                        "FROM Friends f " +
                        "INNER JOIN Users u ON (f.User1ID = u.UserID OR f.User2ID = u.UserID) " +
                        "WHERE (f.User1ID = ? OR f.User2ID = ?) " +
                        "AND u.UserID != ?"
        );

        ps.setInt(1, userId);
        ps.setInt(2, userId);
        ps.setInt(3, userId); // Exclude current user from the result

        ResultSet rs = ps.executeQuery();

        List<Friend> friends = new ArrayList<>();
        System.out.printf(friends.toString());
        while (rs.next()) {
            friends.add(new Friend(
                    rs.getInt("FriendID"),
                    rs.getString("FriendName") // ✅ Include Friend Name
            ));
        }

        return friends;
    }

}
