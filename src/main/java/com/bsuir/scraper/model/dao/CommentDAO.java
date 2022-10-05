package com.bsuir.scraper.model.dao;

import com.bsuir.scraper.database.ConnectionCreator;
import com.bsuir.scraper.exception.DAOException;
import com.bsuir.scraper.model.entity.CustomComment;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CommentDAO extends AbstractDAO<CustomComment> {
    private static final String SQL_INSERT_COMMENT = "INSERT INTO comments (text, datetime, article_id, author_id) VALUES (?, ?, ?, ?);";
    private static final String SQL_SELECT_ALL_COMMENTS = "SELECT comment_id, comments.text, comments.datetime, articles.article_id, authors.author_id FROM comments "
            + "JOIN articles ON comments.article_id = articles.article_id "
            + "JOIN authors ON comments.author_id = authors.author_id;";
    private static final String SQL_SELECT_COMMENTS_BY_ARTICLE_ID = "SELECT comment_id, comments.text, comments.datetime, authors.author_id FROM comments "
            + "JOIN articles ON comments.article_id = articles.article_id "
            + "JOIN authors ON comments.author_id = authors.author_id "
            + "WHERE articles.article_id = ?;";
    private static final String SQL_DELETE_COMMENT_BY_ID = "DELETE FROM comments WHERE comment_id = ?;";
    private static final int SINGLE_UPDATE = 1;

    @Override
    public boolean create(CustomComment entity) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            setConnection(ConnectionCreator.createConnection());
            preparedStatement = connection.prepareStatement(SQL_INSERT_COMMENT);
            preparedStatement.setString(1, entity.getText());
            preparedStatement.setDate(2, Date.valueOf(entity.getDatetime()));
            preparedStatement.setLong(3, entity.getArticle().getArticleId());
            preparedStatement.setLong(4, entity.getAuthor().getAuthorId());
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
    public List<CustomComment> findAll() throws DAOException {
        List<CustomComment> comments = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        AuthorDAO authorDAO = new AuthorDAO();
        ArticleDAO articleDAO = new ArticleDAO();
        try {
            setConnection(ConnectionCreator.createConnection());
            preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_COMMENTS);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                CustomComment comment = new CustomComment();
                long commentId = resultSet.getLong(1);
                comment.setCommentId(commentId);
                String commentText = resultSet.getString(2);
                comment.setText(commentText);
                LocalDate commentDatetime = resultSet.getDate(3).toLocalDate();
                comment.setDatetime(commentDatetime);
                long articleId = resultSet.getLong(4);
                articleDAO.findArticleById(articleId).ifPresent(comment::setArticle);
                long authorId = resultSet.getLong(5);
                authorDAO.findAuthorById(authorId).ifPresent(comment::setAuthor);
                comments.add(comment);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DAOException(e.getMessage());
        } finally {
            closePreparedStatement(preparedStatement);
            closeConnection();
        }
        return comments;
    }

    public List<CustomComment> findCommentsByArticleId(long articleId) throws DAOException {
        List<CustomComment> comments = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        AuthorDAO authorDAO = new AuthorDAO();
        ArticleDAO articleDAO = new ArticleDAO();
        try {
            setConnection(ConnectionCreator.createConnection());
            preparedStatement = connection.prepareStatement(SQL_SELECT_COMMENTS_BY_ARTICLE_ID);
            preparedStatement.setLong(1, articleId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                CustomComment comment = new CustomComment();
                long commentId = resultSet.getLong(1);
                comment.setCommentId(commentId);
                String commentText = resultSet.getString(2);
                comment.setText(commentText);
                LocalDate commentDatetime = resultSet.getDate(3).toLocalDate();
                comment.setDatetime(commentDatetime);
                articleDAO.findArticleById(articleId).ifPresent(comment::setArticle);
                long authorId = resultSet.getLong(4);
                authorDAO.findAuthorById(authorId).ifPresent(comment::setAuthor);
                comments.add(comment);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DAOException(e.getMessage());
        } finally {
            closePreparedStatement(preparedStatement);
            closeConnection();
        }
        return comments;
    }

    @Override
    public boolean delete(long entityId) throws DAOException {
        PreparedStatement preparedStatement = null;
        try {
            setConnection(ConnectionCreator.createConnection());
            preparedStatement = connection.prepareStatement(SQL_DELETE_COMMENT_BY_ID);
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
