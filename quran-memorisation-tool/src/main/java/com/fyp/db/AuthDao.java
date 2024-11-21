package com.fyp.db;

import com.fyp.cli.AuthRole;
import com.fyp.cli.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class AuthDao {

    private  DatabaseConnector databaseConnector;

    public AuthDao(DatabaseConnector databaseConnector) {
        this.databaseConnector=databaseConnector;
    }

    public User getUserByEmail(String email) throws SQLException {
        Connection c = databaseConnector.getConnection();
        PreparedStatement ps = c.prepareStatement(
                "SELECT u.UserID, u.Email, u.Password, u.RoleID, r.Role_Name " +
                        "FROM Users u " +
                        "INNER JOIN Auth_Roles r ON u.RoleID = r.RoleID " +
                        "WHERE u.Email = '" + email + "'"
        );
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            int roleId = rs.getInt("RoleID");
            String roleName = rs.getString("Role_Name");
            AuthRole authRole = new AuthRole(roleId, roleName);
            User user = new User(
                    rs.getInt("UserID"),
                    rs.getString("Email"),
                    authRole,
                    rs.getString("Password")
            );
            return user;
        }

        return null;
    }

    public void register(String username, String password, int role) throws SQLException {
        Connection c = databaseConnector.getConnection();

        PreparedStatement ps = c.prepareStatement("INSERT INTO `Users` (Email, Password, RoleID) VALUES (?, ?, ?)");

        ps.setString(1, username);
        ps.setString(2, password);
        ps.setInt(3, role);

        ps.executeUpdate();
    }
}
