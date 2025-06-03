package org.example.persistence;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionSingleton {
    private static final String DB_URL = "jdbc:h2:./data/trenicaldb"; // File ./data/trenicaldb.mv.db
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    private static Connection conn;

    public static Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()){
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        }
        return conn;
    }
}