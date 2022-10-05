package com.bsuir.scraper.database;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionCreator {
    static Logger logger = LogManager.getLogger();
    private static final Properties properties = new Properties();
    private static final String DATABASE_PROPERTIES_PATH = "database/database.properties";
    private static final String DRIVER_NAME_PROPERTY_KEY = "db.driver";
    private static final String DATABASE_URL_PROPERTY_KEY = "db.url";
    private static final String DATABASE_URL;

    static {
        try {
            InputStream inputStream = ConnectionCreator.class.getClassLoader().getResourceAsStream(DATABASE_PROPERTIES_PATH);
            properties.load(inputStream);
            String driverName = properties.getProperty(DRIVER_NAME_PROPERTY_KEY);
            Class.forName(driverName);
        } catch (IOException | ClassNotFoundException e) {
            logger.log(Level.FATAL, "Database properties or driver not found", e);
            throw new RuntimeException("Database properties or driver not found", e);
        }
        DATABASE_URL = properties.getProperty(DATABASE_URL_PROPERTY_KEY);
    }

    public static Connection createConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL, properties);
    }
}
