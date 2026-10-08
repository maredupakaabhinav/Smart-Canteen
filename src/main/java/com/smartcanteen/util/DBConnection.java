package com.smartcanteen.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Gives the DAO classes a JDBC connection to MySQL.
 *
 * The database settings are NOT written in the code. They are read, in this order, from:
 *   1. environment variables  DB_URL, DB_USERNAME, DB_PASSWORD
 *   2. Java system properties (-DDB_URL=...)
 *   3. a db.properties file on the classpath (copy db.properties.example to db.properties)
 */
public class DBConnection {

    private static final Logger LOGGER = Logger.getLogger(DBConnection.class.getName());

    private static final String DEFAULT_URL =
            "jdbc:mysql://localhost:3306/smart_canteen?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8";
    private static final String DEFAULT_USERNAME = "root";

    private static final Properties FILE_SETTINGS = loadSettingsFile();

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "MySQL JDBC driver not found. Check the Maven dependency.", e);
        }
    }

    private DBConnection() {
        // utility class - no objects needed
    }

    /** Opens a new connection. The caller must close it (use try-with-resources). */
    public static Connection getConnection() throws SQLException {
        String url = readSetting("DB_URL", DEFAULT_URL);
        String username = readSetting("DB_USERNAME", DEFAULT_USERNAME);
        String password = readSetting("DB_PASSWORD", "");
        return DriverManager.getConnection(url, username, password);
    }

    /** Used at start-up to print a clear message in the Tomcat console. */
    public static boolean isDatabaseAvailable() {
        try (Connection connection = getConnection()) {
            return connection.isValid(2);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Cannot connect to MySQL: " + e.getMessage());
            return false;
        }
    }

    private static String readSetting(String name, String defaultValue) {
        String value = System.getenv(name);
        if (isEmpty(value)) {
            value = System.getProperty(name);
        }
        if (isEmpty(value)) {
            value = FILE_SETTINGS.getProperty(name);
        }
        return isEmpty(value) ? defaultValue : value.trim();
    }

    private static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    private static Properties loadSettingsFile() {
        Properties properties = new Properties();
        try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Could not read db.properties", e);
        }
        return properties;
    }
}
