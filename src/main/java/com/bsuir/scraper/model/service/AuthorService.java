package com.bsuir.scraper.model.service;

import com.bsuir.scraper.exception.DAOException;
import com.bsuir.scraper.exception.ServiceException;
import com.bsuir.scraper.model.dao.AuthorDAO;
import com.bsuir.scraper.model.entity.Author;
import com.bsuir.scraper.util.EntityParameter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class AuthorService {
    static Logger logger = LogManager.getLogger();

    public boolean create(Map<String, String> authorData) throws ServiceException {
        AuthorDAO authorDAO = new AuthorDAO();
        Author author = new Author();
        author.setFullName(authorData.get(EntityParameter.FULL_NAME_KEY));
        author.setLink(authorData.get(EntityParameter.AUTHOR_LINK_KEY));
        try {
            return authorDAO.create(author);
        } catch (DAOException e) {
            logger.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    public List<Author> findAll() throws ServiceException {
        AuthorDAO authorDAO = new AuthorDAO();
        try {
            return authorDAO.findAll();
        } catch (DAOException e) {
            logger.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    public Optional<Author> findAuthorByLink(String link) throws ServiceException {
        AuthorDAO authorDAO = new AuthorDAO();
        try {
            return authorDAO.findAuthorByLink(link);
        } catch (DAOException e) {
            logger.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    public Set<Author> findAuthorsByArticleId(long articleId) throws ServiceException {
        AuthorDAO authorDAO = new AuthorDAO();
        try {
            return authorDAO.findAuthorsByArticleId(articleId);
        } catch (DAOException e) {
            logger.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    public boolean delete(long authorId) throws ServiceException {
        AuthorDAO authorDAO = new AuthorDAO();
        try {
            return authorDAO.delete(authorId);
        } catch (DAOException e) {
            logger.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }
}
