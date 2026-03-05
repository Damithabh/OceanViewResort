package util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Utility for Database Connection Management.
 * Modified to load credentials from a secure properties file.
 * HANDLES THREAD-SAFE CONNECTION GENERATION.
 */
public class DBConnectionPool {

    private static DBConnectionPool instance;
    private final Properties properties = new Properties();

    private DBConnectionPool() {
        try {
            // Register MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Load DB properties securely
            try (InputStream input = getClass().getClassLoader().getResourceAsStream("util/db.properties")) {
                if (input == null) {
                    System.err.println("❌ Unable to find util/db.properties inside the classpath.");
                    // Fallback to absolute path or manual load if classpath loading fails in
                    // certain environments
                } else {
                    properties.load(input);
                    System.out.println("✅ Database properties loaded successfully.");
                }
            }
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Error: MySQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("❌ Error loading database properties.");
            e.printStackTrace();
        }
    }

    /**
     * Retrieve the singleton factory instance.
     */
    public static synchronized DBConnectionPool getInstance() {
        if (instance == null) {
            instance = new DBConnectionPool();
        }
        return instance;
    }

    /**
     * Get a NEW active connection object for the calling thread.
     * Credentials are pulled from the configuration properties.
     * 
     * @return Connection A new MySQL JDBC connection.
     * @throws SQLException If database access error occurs.
     */
    public Connection getConnection() throws SQLException {
        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.user");
        String pass = properties.getProperty("db.password");

        // Fallback if properties failed to load
        if (url == null) {
            url = "jdbc:mysql://127.0.0.1:3306/ocean_view_resort?useSSL=false&serverTimezone=UTC";
            user = "root";
            pass = "root";
        }

        return DriverManager.getConnection(url, user, pass);
    }
}
