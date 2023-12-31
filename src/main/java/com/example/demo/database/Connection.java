package com.example.demo.database;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Connection {
    private static java.sql.Connection connection;

    public static java.sql.Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            initializeConnection();
        }
        return connection;
    }

    private static void initializeConnection() {
        Properties properties = Config.getProperties();

        String dbUrl = properties.getProperty("db.url");
        String dbUsername = properties.getProperty("db.username");
        String dbPassword = properties.getProperty("db.password");

        try {
            connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            System.out.println("Connected to the database.");
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database.");
        }
    }
}
