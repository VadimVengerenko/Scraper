package com.bsuir.scraper.model.dao;

import com.bsuir.scraper.database.ConnectionCreator;
import com.bsuir.scraper.exception.DAOException;
import com.bsuir.scraper.model.entity.Article;
import com.bsuir.scraper.model.entity.Author;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class ArticleDAO extends AbstractDAO<Article> {
    private static final String SQL_INSERT_ARTICLE = "INSERT INTO articles (title, text, datetime, link) VALUES (?, ?, ?, ?);";
    private static final String SQL_INSERT_ARTICLE_AUTHORS = "INSERT INTO authors_articles (author_id, article_id) VALUES (?, ?);";
    private static final String SQL_SELECT_ALL_ARTICLES = "SELECT article_id, title, text, datetime, link FROM articles;";
    private static final String SQL_SELECT_ARTICLES_BY_AUTHOR_ID = "SELECT articles.article_id, title, text, datetime, articles.link FROM articles "
            + "JOIN authors_articles ON articles.article_id = authors_articles.article_id "
            + "JOIN authors ON authors.author_id = authors_articles.author_id "
            + "WHERE authors.author_id = ?;";
    private static final String SQL_SELECT_ARTICLE_BY_ID = "SELECT title, text, datetime, link FROM articles "
            + "WHERE article_id = ?;";
    private static final String SQL_SELECT_ARTICLE_BY_LINK = "SELECT article_id, title, text, datetime FROM articles "
            + "WHERE link = ?;";
    private static final String SQL_DELETE_ARTICLE_BY_ID = "DELETE FROM articles WHERE article_id = ?;";
    private static final int SINGLE_UPDATE = 1;

    @Override
    public boolean create(Article entity) throws DAOException {
        boolean result;
        PreparedStatement preparedStatementArticle = null;
        AuthorDAO authorDAO = new AuthorDAO();
        try {
            setConnection(ConnectionCreator.createConnection());
            preparedStatementArticle = connection.prepareStatement(SQL_INSERT_ARTICLE);
            preparedStatementArticle.setString(1, entity.getTitle());
            preparedStatementArticle.setString(2, entity.getText());
            preparedStatementArticle.setDate(3, Date.valueOf(entity.getDatetime()));
            preparedStatementArticle.setString(4, entity.getLink());
            result = preparedStatementArticle.executeUpdate() == SINGLE_UPDATE;
            Optional<Article> optionalArticle = findArticleByLink(entity.getLink());
            optionalArticle.ifPresent(article -> entity.setArticleId(article.getArticleId()));
            if (result) {
                Set<Author> authors = entity.getAuthors();
                for (Author author : authors) {
                    Optional<Author> optionalAuthor = authorDAO.findAuthorByLink(author.getLink());
                    if (optionalAuthor.isPresent()) {
                        author.setAuthorId(optionalAuthor.get().getAuthorId());
                        result &= createArticleAuthors(author.getAuthorId(), entity.getArticleId());
                    } else if (authorDAO.create(author)) {
                        optionalAuthor = authorDAO.findAuthorByLink(author.getLink());
                        optionalAuthor.ifPresent(tempAuthor -> author.setAuthorId(tempAuthor.getAuthorId()));
                        result &= createArticleAuthors(author.getAuthorId(), entity.getArticleId());
                    }
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DAOException(e.getMessage());
        } finally {
            closePreparedStatement(preparedStatementArticle);
            closeConnection();
        }
        return result;
    }

    public boolean createArticleAuthors(long authorId, long articleId) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            setConnection(ConnectionCreator.createConnection());
            preparedStatement = connection.prepareStatement(SQL_INSERT_ARTICLE_AUTHORS);
            preparedStatement.setLong(1, authorId);
            preparedStatement.setLong(2, articleId);
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
    public List<Article> findAll() throws DAOException {
        List<Article> articles = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        AuthorDAO authorDAO = new AuthorDAO();
        try {
            setConnection(ConnectionCreator.createConnection());
            preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_ARTICLES);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long articleId = resultSet.getLong(1);
                String title = resultSet.getString(2);
                String text = resultSet.getString(3);
                LocalDate datetime = resultSet.getDate(4).toLocalDate();
                String link = resultSet.getString(5);
                Set<Author> authors = authorDAO.findAuthorsByArticleId(articleId);
                articles.add(new Article(articleId, title, text, datetime, link, authors));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DAOException(e.getMessage());
        } finally {
            closePreparedStatement(preparedStatement);
            closeConnection();
        }
        return articles;
    }

    public Set<Article> findArticlesByAuthorId(long authorId) throws DAOException {
        PreparedStatement preparedStatement = null;
        Set<Article> articles = new HashSet<>();
        AuthorDAO authorDAO = new AuthorDAO();
        try {
            setConnection(ConnectionCreator.createConnection());
            preparedStatement = connection.prepareStatement(SQL_SELECT_ARTICLES_BY_AUTHOR_ID);
            preparedStatement.setLong(1, authorId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long articleId = resultSet.getLong(1);
                String title = resultSet.getString(2);
                String text = resultSet.getString(3);
                LocalDate datetime = resultSet.getDate(4).toLocalDate();
                String link = resultSet.getString(5);
                Set<Author> authors = authorDAO.findAuthorsByArticleId(articleId);
                articles.add(new Article(articleId, title, text, datetime, link, authors));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DAOException(e.getMessage());
        } finally {
            closePreparedStatement(preparedStatement);
            closeConnection();
        }
        return articles;
    }

    public Optional<Article> findArticleById(long articleId) throws DAOException {
        PreparedStatement preparedStatement = null;
        Optional<Article> article = Optional.empty();
        AuthorDAO authorDAO = new AuthorDAO();
        try {
            setConnection(ConnectionCreator.createConnection());
            preparedStatement = connection.prepareStatement(SQL_SELECT_ARTICLE_BY_ID);
            preparedStatement.setLong(1, articleId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String title = resultSet.getString(1);
                String text = resultSet.getString(2);
                LocalDate datetime = resultSet.getDate(3).toLocalDate();
                String link = resultSet.getString(4);
                Set<Author> authors = authorDAO.findAuthorsByArticleId(articleId);
                article = Optional.of(new Article(articleId, title, text, datetime, link, authors));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DAOException(e.getMessage());
        } finally {
            closePreparedStatement(preparedStatement);
            closeConnection();
        }
        return article;
    }

    public Optional<Article> findArticleByLink(String link) throws DAOException {
        PreparedStatement preparedStatement = null;
        Optional<Article> article = Optional.empty();
        AuthorDAO authorDAO = new AuthorDAO();
        try {
            setConnection(ConnectionCreator.createConnection());
            preparedStatement = connection.prepareStatement(SQL_SELECT_ARTICLE_BY_LINK);
            preparedStatement.setString(1, link);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                long articleId = resultSet.getLong(1);
                String title = resultSet.getString(2);
                String text = resultSet.getString(3);
                LocalDate datetime = resultSet.getDate(4).toLocalDate();
                Set<Author> authors = authorDAO.findAuthorsByArticleId(articleId);
                article = Optional.of(new Article(articleId, title, text, datetime, link, authors));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DAOException(e.getMessage());
        } finally {
            closePreparedStatement(preparedStatement);
            closeConnection();
        }
        return article;
    }

    @Override
    public boolean delete(long entityId) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            setConnection(ConnectionCreator.createConnection());
            preparedStatement = connection.prepareStatement(SQL_DELETE_ARTICLE_BY_ID);
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
