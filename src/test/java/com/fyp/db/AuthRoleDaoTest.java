package com.fyp.db;


import com.fyp.cli.AuthRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;




@ExtendWith(MockitoExtension.class)
public class AuthRoleDaoTest {
    private DatabaseConnector databaseConnector = Mockito.mock(DatabaseConnector.class);
    private Connection connection = Mockito.mock(Connection.class);
    private PreparedStatement statement = Mockito.mock(PreparedStatement.class);
    private ResultSet resultSet = Mockito.mock(ResultSet.class);


    private AuthRoleDao authRoleDao;

    @BeforeEach
    void setUp() {
        authRoleDao = new AuthRoleDao();
    }


    @Test
    void getAuthRoles_ShouldReturnListOfAuthRoles() throws SQLException {
        AuthRole expectedRole = new AuthRole(1, "Admin");

        DatabaseConnector.setConn(connection);

        Mockito.when(connection.prepareStatement(anyString())).thenReturn(statement);
        Mockito.when(statement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);
        Mockito.when(resultSet.getInt(1)).thenReturn(1);
        Mockito.when(resultSet.getString(2)).thenReturn("Admin");

        List<AuthRole> actualRoles = authRoleDao.getAuthRoles();

        List<AuthRole> expectedRoles = new ArrayList<>();
        expectedRoles.add(expectedRole);

        assertEquals(expectedRoles.get(0).getRoleID(), actualRoles.get(0).getRoleID());
        assertEquals(expectedRoles.get(0).getRole_name(), actualRoles.get(0).getRole_name());
    }

    @Test
    void getAuthRoles_ShouldThrowSQLException_WhenSQLExceptionOccurs() throws SQLException {

        DatabaseConnector.setConn(connection);

        Mockito.when(connection.prepareStatement(anyString())).thenReturn(statement);
        Mockito.when(statement.executeQuery()).thenThrow(SQLException.class);

        assertThrows(SQLException.class, () -> authRoleDao.getAuthRoles());

    }

    @Test
    void getRoleById_Success() throws SQLException {
        int roleId = 1;
        String roleName = "Admin";

        DatabaseConnector.setConn(connection);

        Mockito.when(connection.prepareStatement(anyString())).thenReturn(statement);
        Mockito.when(statement.executeQuery()).thenReturn(resultSet);

        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);
        Mockito.when(resultSet.getInt("RoleID")).thenReturn(roleId);
        Mockito.when(resultSet.getString("Role_Name")).thenReturn(roleName);


        AuthRole authRole = authRoleDao.getRoleById(roleId);

        assertNotNull(authRole);
        assertEquals(roleId, authRole.getRoleID());
        assertEquals(roleName, authRole.getRole_name());
    }

    @Test
    public void testGetRoleById_NotFound() throws SQLException {
        int roleId = 2;

        DatabaseConnector.setConn(connection);

        Mockito.when(connection.prepareStatement(anyString())).thenReturn(statement);
        Mockito.when(statement.executeQuery()).thenReturn(resultSet);

        Mockito.when(resultSet.next()).thenReturn(false);

        AuthRole authRole = authRoleDao.getRoleById(roleId);

        assertNull(authRole);
    }

    @Test
    public void testGetRoleById_SQLException() throws SQLException {
        int roleId = 3;
        DatabaseConnector.setConn(connection);
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(statement);


        Mockito.when(statement.executeQuery()).thenThrow(SQLException.class);

        assertThrows(SQLException.class, () -> authRoleDao.getRoleById(roleId));
    }


}
