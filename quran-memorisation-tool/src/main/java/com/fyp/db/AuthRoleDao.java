package com.fyp.db;

import com.fyp.cli.AuthRole;
import com.fyp.client.FailedToGetAuthRoles;
import com.fyp.client.FailedToGetRoleException;

import java.security.Principal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuthRoleDao {
    private final DatabaseConnector databaseConnector = new DatabaseConnector();

    public List<AuthRole> getAuthRoles() throws SQLException {
        Connection c = DatabaseConnector.getConnection();
        String query = "SELECT * FROM Auth_Roles";

        PreparedStatement st = c.prepareStatement(query);

        ResultSet rs = st.executeQuery();

        List<AuthRole> authRoles = new ArrayList<>();

        while(rs.next()){
            authRoles.add(new AuthRole(
                    rs.getInt(1),
                    rs.getString(2)
            ));
        }
        return authRoles;
    }


    public AuthRole getRoleById(int roleId) throws SQLException {
        Connection c = databaseConnector.getConnection();
        String query = "SELECT * FROM Auth_Roles WHERE RoleID = ?";
        try (PreparedStatement statement = c.prepareStatement(query)) {
            statement.setInt(1, roleId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("RoleID");
                    String roleName = resultSet.getString("Role_Name");

                    return new AuthRole(id, roleName);
                } else {
                    return null;
                }
            }
        }
    }
}
