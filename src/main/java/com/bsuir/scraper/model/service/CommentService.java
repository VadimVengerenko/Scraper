package com.bsuir.scraper.model.service;

import com.bsuir.scraper.exception.DAOException;
import com.bsuir.scraper.exception.ServiceException;
import com.bsuir.scraper.model.dao.ArticleDAO;
import com.bsuir.scraper.model.dao.AuthorDAO;
import com.bsuir.scraper.model.dao.CommentDAO;
import com.bsuir.scraper.model.entity.CustomComment;
import com.bsuir.scraper.util.EntityParameter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class CommentService {
    static Logger logger = LogManager.getLogger();

    public boolean create(Map<String, String> commentData) throws ServiceException {
        CommentDAO commentDAO = new CommentDAO();
        ArticleDAO articleDAO = new ArticleDAO();
        AuthorDAO authorDAO = new AuthorDAO();
        CustomComment comment = new CustomComment();
        comment.setText(commentData.get(EntityParameter.COMMENT_TEXT_KEY));
        comment.setDatetime(LocalDate.parse(commentData.get(EntityParameter.COMMENT_DATETIME_KEY), DateTimeFormatter.ofPattern("dd.MM.uuuu HH:mm")));
        long articleId = Long.parseLong(commentData.get(EntityParameter.ARTICLE_ID_KEY));
        long authorId = Long.parseLong(commentData.get(EntityParameter.AUTHOR_ID_KEY));
        try {
            articleDAO.findArticleById(articleId).ifPresent(comment::setArticle);
            authorDAO.findAuthorById(authorId).ifPresent(comment::setAuthor);
            return commentDAO.create(comment);
        } catch (DAOException e) {
            logger.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    public List<CustomComment> findAll() throws ServiceException {
        CommentDAO commentDAO = new CommentDAO();
        try {
            return commentDAO.findAll();
        } catch (DAOException e) {
            logger.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    public List<CustomComment> findCommentsByArticleId(long articleId) throws ServiceException {
        CommentDAO commentDAO = new CommentDAO();
        try {
            return commentDAO.findCommentsByArticleId(articleId);
        } catch (DAOException e) {
            logger.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    public boolean delete(long commentId) throws ServiceException {
        CommentDAO commentDAO = new CommentDAO();
        try {
            return commentDAO.delete(commentId);
        } catch (DAOException e) {
            logger.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }
}
