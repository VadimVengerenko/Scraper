package com.bsuir.scraper.model.dao;

import com.bsuir.scraper.exception.DAOException;
import com.bsuir.scraper.model.entity.CustomEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public abstract class AbstractDAO<T extends CustomEntity> {
    static Logger logger = LogManager.getLogger();
    protected Connection connection;

    public abstract boolean create(T entity) throws DAOException;
    public abstract List<T> findAll() throws DAOException;
    public abstract boolean delete(long entityId) throws DAOException;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void closePreparedStatement(PreparedStatement preparedStatement) {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
        }
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
        }
    }
}
