package com.fyp.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private static Connection conn;
    public static Connection getConnection() throws SQLException {
        String user, password, host, name;

        if(conn != null && !conn.isClosed()){
            return conn;
        }

        try{
            user = System.getenv("DB_USERNAME");
            password = System.getenv("DB_PASSWORD");
            host = System.getenv("DB_HOST");
            name = System.getenv("DB_NAME");

            if(user == null || password == null || host == null){
                throw new Exception("Properties file must exist " +
                        "and must contain user, password, name and host properties");
            }

            conn = DriverManager.getConnection("jdbc:mysql://" + host + "/" + name + "?useSSL=false&allowPublicKeyRetrieval=true", user, password);
            return conn;

        } catch (Exception e) {
            System.err.println(e.getMessage());

            throw new IllegalArgumentException();
        }
    }
    public static void setConn(Connection connection){
        conn = connection;
    }
}