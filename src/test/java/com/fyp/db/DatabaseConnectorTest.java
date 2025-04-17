package com.fyp.db;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnectorTest {

    private Connection conn = Mockito.mock(Connection.class);
    @Test
    void getConnection_ShouldReturnConn_WhenConnIsNotNullAndNotClosed() throws SQLException {
        DatabaseConnector.setConn(conn);

        Connection returnedConn = DatabaseConnector.getConnection();

        Assertions.assertEquals(conn, returnedConn);
    }
}

