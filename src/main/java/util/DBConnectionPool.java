package util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Singleton Pattern implementation for Database Connection Management.
 * Provides thread-safe, centralized connection creation using credentials
 * loaded from an externalized properties file.
 * 
 * Why Singleton: Only one connection factory instance is needed per JVM;
 * multiple instances would waste resources and make configuration inconsistent.
 * 
 * // Learned from https://refactoring.guru/design-patterns/singleton
 * 
 * @author Ocean View Resort Dev Team
 */
public class DBConnectionPool {

    private static final Logger LOGGER = AppLogger.getLogger(DBConnectionPool.class);

    /** The single instance of this class (volatile for double-checked locking). */
    private static volatile DBConnectionPool instance;

    /** Loaded database configuration properties. */
    private final Properties properties = new Properties();

    /**
     * Private constructor — loads JDBC driver and reads configuration.
     * Why private: Prevents external instantiation, enforcing the Singleton
     * contract.
     */
    private DBConnectionPool() {
        try {
            // Register MySQL JDBC Driver explicitly
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Load DB properties from classpath
            try (InputStream input = getClass().getClassLoader().getResourceAsStream("util/db.properties")) {
                if (input == null) {
                    LOGGER.severe("Unable to find util/db.properties in classpath. Using fallback credentials.");
                } else {
                    properties.load(input);
                    LOGGER.info("Database properties loaded successfully from util/db.properties.");
                }
            }
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "MySQL JDBC Driver not found on classpath.", e);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading database properties file.", e);
        }
    }

    /**
     * Retrieves the singleton instance using double-checked locking.
     * Why: Thread-safe lazy initialization without synchronizing every call.
     *
     * @return The single DBConnectionPool instance
     */
    public static DBConnectionPool getInstance() {
        if (instance == null) {
            synchronized (DBConnectionPool.class) {
                if (instance == null) {
                    instance = new DBConnectionPool();
                }
            }
        }
        return instance;
    }

    /**
     * Creates and returns a new JDBC connection for the calling thread.
     * Each caller is responsible for closing the connection after use.
     *
     * @return A new active MySQL JDBC Connection
     * @throws SQLException If a database access error occurs
     */
    public Connection getConnection() throws SQLException {
        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.user");
        String pass = properties.getProperty("db.password");

        // Fallback credentials if properties file failed to load
        if (url == null) {
            url = "jdbc:mysql://127.0.0.1:3306/ocean_view_resort?useSSL=false&serverTimezone=UTC";
            user = "root";
            pass = "root";
        }

        return DriverManager.getConnection(url, user, pass);
    }
}
