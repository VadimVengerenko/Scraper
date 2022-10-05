package com.bsuir.scraper.database;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ConnectionCreatorTest {
    @Test
    public void createConnection() throws SQLException {
        Connection connection = ConnectionCreator.createConnection();
        assertNotNull(connection);
        connection.close();
    }
}
