package com.bsuir.scraper.model.service;

import com.bsuir.scraper.exception.DAOException;
import com.bsuir.scraper.exception.ServiceException;
import com.bsuir.scraper.model.dao.ArticleDAO;
import com.bsuir.scraper.model.entity.Article;
import com.bsuir.scraper.model.entity.Author;
import com.bsuir.scraper.util.EntityParameter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class ArticleService {
    static Logger logger = LogManager.getLogger();

    public boolean create(Map<String, String> articleData, Set<Author> authors) throws ServiceException {
        ArticleDAO articleDAO = new ArticleDAO();
        Article article = new Article();
        article.setTitle(articleData.get(EntityParameter.TITLE_KEY));
        article.setText(articleData.get(EntityParameter.ARTICLE_TEXT_KEY));
        article.setDatetime(LocalDate.parse(articleData.get(EntityParameter.ARTICLE_DATETIME_KEY), DateTimeFormatter.ISO_DATE_TIME));
        article.setLink(articleData.get(EntityParameter.ARTICLE_LINK_KEY));
        article.setAuthors(authors);
        try {
            return articleDAO.create(article);
        } catch (DAOException e) {
            logger.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    public List<Article> findAll() throws ServiceException {
        ArticleDAO articleDAO = new ArticleDAO();
        try {
            return articleDAO.findAll();
        } catch (DAOException e) {
            logger.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    public Optional<Article> findArticleByLink(String link) throws ServiceException {
        ArticleDAO articleDAO = new ArticleDAO();
        try {
            return articleDAO.findArticleByLink(link);
        } catch (DAOException e) {
            logger.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    public boolean delete(long articleId) throws ServiceException {
        ArticleDAO articleDAO = new ArticleDAO();
        try {
            return articleDAO.delete(articleId);
        } catch (DAOException e) {
            logger.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }
}
