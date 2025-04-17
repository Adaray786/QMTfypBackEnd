package com.fyp.db;

import com.fyp.cli.AuthRole;
import com.fyp.cli.LoginRequest;
import com.fyp.cli.User;
import com.fyp.client.FailedLoginException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class AuthDaoTest {
    private DatabaseConnector databaseConnector = Mockito.mock(DatabaseConnector.class);
    private Connection connection = Mockito.mock(Connection.class);
    private PreparedStatement statement = Mockito.mock(PreparedStatement.class);
    private ResultSet resultSet = Mockito.mock(ResultSet.class);


    private AuthDao authDao = new AuthDao(databaseConnector);


    @Test
    void validLogin_ShouldReturnUser_WhenLoginRequestIsValid() throws SQLException, FailedLoginException {
        String email = "email@email.com";
        String salt = BCrypt.gensalt();
        LoginRequest validLoginRequest = new LoginRequest("email@email.com", "password");
        User expectedUser = new User(1, "email@email.com", new AuthRole(1, "Admin"));

        String preparedStatement = "SELECT u.UserID, u.Email, u.Password, u.RoleID, r.Role_Name " +
                "FROM Users u " +
                "INNER JOIN Auth_Roles r ON u.RoleID = r.RoleID " +
                "WHERE u.Email = '" + email + "'";
        DatabaseConnector.setConn(connection);
        Mockito.when(connection.prepareStatement(preparedStatement)).thenReturn(statement);
        Mockito.when(statement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getInt("UserID")).thenReturn(1);
        Mockito.when(resultSet.getString("Email")).thenReturn("email@email.com");
        Mockito.when(resultSet.getString("Password")).thenReturn(BCrypt.hashpw("password", salt));
        Mockito.when(resultSet.getInt("RoleID")).thenReturn(1);

        User user = authDao.getUserByEmail(validLoginRequest.getEmail());

        assertEquals(expectedUser.getUserId(),user.getUserId());
        assertEquals(expectedUser.getEmail(),user.getEmail());
    }

    @Test
    void validLogin_ShouldReturnException_WhenLoginRequestIsInvalid() throws SQLException {
        String email = "email@email.com";
        LoginRequest invalidLoginRequest = new LoginRequest(email, "password");

        String preparedStatement = "SELECT u.UserID, u.Email, u.Password, u.RoleID, r.Role_Name " +
                "FROM Users u " +
                "INNER JOIN Auth_Roles r ON u.RoleID = r.RoleID " +
                "WHERE u.Email = '" + email + "'";
        DatabaseConnector.setConn(connection);
        Mockito.when(connection.prepareStatement(preparedStatement)).thenReturn(statement);
        Mockito.when(statement.executeQuery()).thenThrow(FailedLoginException.class);

        assertThrows(FailedLoginException.class,
                () -> authDao.getUserByEmail(invalidLoginRequest.getEmail()));
    }

    @Test
    void validLogin_ShouldThrowSQLException_WhenSQLExceptionOccurs() throws SQLException {
        String email = "email@email.com";
        LoginRequest validLoginRequest = new LoginRequest(email, "password");

        String preparedStatement = "SELECT u.UserID, u.Email, u.Password, u.RoleID, r.Role_Name " +
                "FROM Users u " +
                "INNER JOIN Auth_Roles r ON u.RoleID = r.RoleID " +
                "WHERE u.Email = '" + email + "'";
        DatabaseConnector.setConn(connection);
        Mockito.when(connection.prepareStatement(preparedStatement)).thenReturn(statement);
        Mockito.when(statement.executeQuery()).thenThrow(SQLException.class);

        assertThrows(SQLException.class,
                () -> authDao.getUserByEmail(validLoginRequest.getEmail()));
    }

    @Test
    void register_ShouldRegister_WithValidDetails() throws SQLException {
        String email = "user@user.com";
        String password = "Password$";
        int role = 1;

        DatabaseConnector.setConn(connection);
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(statement);
        Mockito.when(statement.executeUpdate()).thenReturn(1);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getString("Email")).thenReturn(email);
        Mockito.when(resultSet.getInt("RoleID")).thenReturn(1);

        authDao.register(email, password, role);
        assertTrue(resultSet.next());

        assertEquals(email, resultSet.getString("Email"));
        assertEquals(role, resultSet.getInt("RoleID"));
    }
}

