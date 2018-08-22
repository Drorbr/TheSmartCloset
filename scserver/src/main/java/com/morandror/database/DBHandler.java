package com.morandror.database;

import com.morandror.scmanager.LoggingController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class DBHandler {

    Logger logger = LogManager.getLogger(LoggingController.class);

    private static DBHandler ourInstance = new DBHandler();
    private final String url = "jdbc:mysql://localhost:3306/scdb?useSSL=false";
    private final String username = "admin";
    private final String password = "Password1!";
    private final String driver = "com.mysql.jdbc.Driver";
    private Connection connection;

    public static DBHandler getInstance() {
        return ourInstance;
    }

    private DBHandler() {
        try {
            Class.forName(driver).newInstance();
            connection = DriverManager.getConnection(url, username, password);
            logger.info("Database connected!");
        } catch (Exception e) {
            logger.error("Cannot connect the database!");
            throw new IllegalStateException("Cannot connect the database!", e);

        }
    }

    public ResultSet executeQuery(String statement) throws SQLException {
        Statement st = connection.createStatement();
        return st.executeQuery(statement);
    }
}
