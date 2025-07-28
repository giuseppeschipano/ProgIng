package org.example.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionSingleton {

  //private static final String DB_URL = "jdbc:h2:mem:trenicaldb;DB_CLOSE_DELAY=-1";
    private static final String DB_URL = "jdbc:h2:file=./data/trenicaldb;AUTO_SERVER=TRUE";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    private static Connection conn;

    public static synchronized Connection getConnection() throws SQLException {
        try {
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        }
        return conn;
    }

    public static synchronized void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                conn = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}