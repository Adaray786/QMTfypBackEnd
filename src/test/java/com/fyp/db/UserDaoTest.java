package com.fyp.db;

import com.fyp.cli.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserDaoTest {

    private Connection connection = Mockito.mock(Connection.class);
    private PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
    private ResultSet resultSet = Mockito.mock(ResultSet.class);

    private UserDao userDao;

    @BeforeEach
    void setUp() throws SQLException {
        DatabaseConnector.setConn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        userDao = new UserDao(new DatabaseConnector());
    }

    @Test
    void searchUsers_ShouldReturnMatchingUsers() throws SQLException {
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("UserID")).thenReturn(2);
        when(resultSet.getString("Email")).thenReturn("test@example.com");

        List<User> users = userDao.searchUsers("test", 1);

        assertEquals(1, users.size());
        User user = users.get(0);
        assertEquals(2, user.getUserId());
        assertEquals("test@example.com", user.getEmail());

        verify(preparedStatement).setString(1, "%test%");
        verify(preparedStatement).setInt(2, 1);
    }

    @Test
    void searchUsers_ShouldReturnEmptyList_WhenNoMatch() throws SQLException {
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        List<User> users = userDao.searchUsers("nomatch", 1);

        assertNotNull(users);
        assertTrue(users.isEmpty());
    }

    @Test
    void getUserById_ShouldReturnUser_WhenFound() throws SQLException {
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("UserID")).thenReturn(3);
        when(resultSet.getString("Email")).thenReturn("found@example.com");

        User user = userDao.getUserById(3);

        assertNotNull(user);
        assertEquals(3, user.getUserId());
        assertEquals("found@example.com", user.getEmail());

        verify(preparedStatement).setInt(1, 3);
    }

    @Test
    void getUserById_ShouldReturnNull_WhenNotFound() throws SQLException {
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        User user = userDao.getUserById(404);

        assertNull(user);
    }
}
