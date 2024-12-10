package servlets;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import constants.IDatabase;

public class DBConnection {

    private DBConnection() {} // Private constructor to prevent instantiation

    public static Connection getCon() throws SQLException {
        try {
            Class.forName(IDatabase.DRIVER_NAME); // Load the driver
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("Driver not found: " + e.getMessage());
        }

        // Always return a new connection
        return DriverManager.getConnection(
                IDatabase.CONNECTION_STRING, 
                IDatabase.USER_NAME, 
                IDatabase.PASSWORD
        );
    }
}
