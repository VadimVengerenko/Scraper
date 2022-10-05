package com.bsuir.scraper.model.dao;

import com.bsuir.scraper.database.ConnectionCreator;
import com.bsuir.scraper.exception.DAOException;
import com.bsuir.scraper.model.entity.Article;
import com.bsuir.scraper.model.entity.Author;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class AuthorDAO extends AbstractDAO<Author> {
    private static final String SQL_INSERT_AUTHOR = "INSERT INTO authors (full_name, link) VALUES (?, ?);";
    private static final String SQL_SELECT_ALL_AUTHORS = "SELECT author_id, full_name, link FROM authors;";
    private static final String SQL_SELECT_AUTHORS_BY_ARTICLE_ID = "SELECT authors.author_id, full_name, authors.link FROM authors "
            + "JOIN authors_articles ON authors.author_id = authors_articles.author_id "
            + "JOIN articles ON articles.article_id = authors_articles.article_id "
            + "WHERE articles.article_id = ?;";
    private static final String SQL_SELECT_AUTHOR_BY_LINK = "SELECT author_id, full_name, link FROM authors WHERE link = ?;";
    private static final String SQL_SELECT_AUTHOR_BY_ID = "SELECT author_id, full_name, link FROM authors WHERE author_id = ?;";
    private static final String SQL_DELETE_AUTHOR_BY_ID = "DELETE FROM authors WHERE author_id = ?;";
    private static final int SINGLE_UPDATE = 1;

    @Override
    public boolean create(Author entity) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            setConnection(ConnectionCreator.createConnection());
            preparedStatement = connection.prepareStatement(SQL_INSERT_AUTHOR);
            preparedStatement.setString(1, entity.getFullName());
            preparedStatement.setString(2, entity.getLink());
            return preparedStatement.executeUpdate() == SINGLE_UPDATE;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DAOException(e.getMessage());
        } finally {
            closePreparedStatement(preparedStatement);
            closeConnection();
        }
    }

    @Override
    public List<Author> findAll() throws DAOException {
        List<Author> authors = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ArticleDAO articleDAO = new ArticleDAO();
        try {
            setConnection(ConnectionCreator.createConnection());
            preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_AUTHORS);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long authorId = resultSet.getLong(1);
                String fullName = resultSet.getString(2);
                String link = resultSet.getString(3);
                Set<Article> articles = articleDAO.findArticlesByAuthorId(authorId);
                authors.add(new Author(authorId, fullName, link, articles));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DAOException(e.getMessage());
        } finally {
            closePreparedStatement(preparedStatement);
            closeConnection();
        }
        return authors;
    }

    public Set<Author> findAuthorsByArticleId(long articleId) throws DAOException {
        PreparedStatement preparedStatement = null;
        Set<Author> authors = new HashSet<>();
        try {
            setConnection(ConnectionCreator.createConnection());
            preparedStatement = connection.prepareStatement(SQL_SELECT_AUTHORS_BY_ARTICLE_ID);
            preparedStatement.setLong(1, articleId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long authorId = resultSet.getLong(1);
                String fullName = resultSet.getString(2);
                String link = resultSet.getString(3);
                authors.add(new Author(authorId, fullName, link, new HashSet<>()));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DAOException(e.getMessage());
        } finally {
            closePreparedStatement(preparedStatement);
            closeConnection();
        }
        return authors;
    }

    public Optional<Author> findAuthorByLink(String link) throws DAOException {
        PreparedStatement preparedStatement = null;
        Optional<Author> author = Optional.empty();
        ArticleDAO articleDAO = new ArticleDAO();
        try {
            setConnection(ConnectionCreator.createConnection());
            preparedStatement = connection.prepareStatement(SQL_SELECT_AUTHOR_BY_LINK);
            preparedStatement.setString(1, link);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                long authorId = resultSet.getLong(1);
                String fullName = resultSet.getString(2);
                String authorLink = resultSet.getString(3);
                Set<Article> articles = articleDAO.findArticlesByAuthorId(authorId);
                author = Optional.of(new Author(authorId, fullName, authorLink, articles));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DAOException(e.getMessage());
        } finally {
            closePreparedStatement(preparedStatement);
            closeConnection();
        }
        return author;
    }

    public Optional<Author> findAuthorById(long authorId) throws DAOException {
        PreparedStatement preparedStatement = null;
        Optional<Author> author = Optional.empty();
        ArticleDAO articleDAO = new ArticleDAO();
        try {
            setConnection(ConnectionCreator.createConnection());
            preparedStatement = connection.prepareStatement(SQL_SELECT_AUTHOR_BY_ID);
            preparedStatement.setLong(1, authorId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                authorId = resultSet.getLong(1);
                String fullName = resultSet.getString(2);
                String link = resultSet.getString(3);
                Set<Article> articles = articleDAO.findArticlesByAuthorId(authorId);
                author = Optional.of(new Author(authorId, fullName, link, articles));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DAOException(e.getMessage());
        } finally {
            closePreparedStatement(preparedStatement);
            closeConnection();
        }
        return author;
    }

    @Override
    public boolean delete(long entityId) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            setConnection(ConnectionCreator.createConnection());
            preparedStatement = connection.prepareStatement(SQL_DELETE_AUTHOR_BY_ID);
            preparedStatement.setLong(1, entityId);
            return preparedStatement.executeUpdate() == SINGLE_UPDATE;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DAOException(e.getMessage());
        } finally {
            closePreparedStatement(preparedStatement);
            closeConnection();
        }
    }
}
