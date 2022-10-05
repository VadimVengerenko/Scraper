package com.bsuir.scraper.model.dao;

import com.bsuir.scraper.exception.DAOException;
import com.bsuir.scraper.model.entity.Article;
import com.bsuir.scraper.model.entity.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ArticleDAOTest {
    @Mock
    private ArticleDAO articleDAO;
    private List<Article> expectedAllArticles;
    private Set<Article> expectedByFirstAuthorIdArticles;
    private Article firstArticle;
    private Article secondArticle;
    private Article newArticle;
    private final boolean EXPECTED_TRUE = true;
    private final long FIRST_ARTICLE_ID = 1;
    private final long THIRD_ARTICLE_ID = 3;
    private final long FIRST_AUTHOR_ID = 0;
    private final String SECOND_ARTICLE_LINK = "secondArticleLink";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        Set<Author> firstArticleAuthors = new HashSet<>(Arrays.asList(new Author(FIRST_AUTHOR_ID), new Author(1)));
        Set<Author> secondArticleAuthors = new HashSet<>(Collections.singletonList(new Author(FIRST_AUTHOR_ID)));
        Set<Author> thirdArticleAuthors = new HashSet<>(Arrays.asList(new Author(2), new Author(3)));
        firstArticle = new Article(FIRST_ARTICLE_ID, "firstArticleLink", firstArticleAuthors);
        secondArticle = new Article(2, SECOND_ARTICLE_LINK, secondArticleAuthors);
        Article thirdArticle = new Article(THIRD_ARTICLE_ID, "thirdArticleLink", thirdArticleAuthors);
        newArticle = new Article("newArticleLink", new HashSet<>(Arrays.asList(new Author(4),
                new Author(5),
                new Author(6))));
        expectedAllArticles = new ArrayList<>(Arrays.asList(firstArticle, secondArticle, thirdArticle));
        expectedByFirstAuthorIdArticles = new HashSet<>(Arrays.asList(firstArticle, secondArticle));
    }

    @Test
    public void createTest() throws DAOException {
        when(articleDAO.create(newArticle)).thenReturn(EXPECTED_TRUE);
        boolean actual = articleDAO.create(newArticle);
        assertEquals(EXPECTED_TRUE, actual);
    }

    @Test
    public void createArticleAuthorsTest() throws DAOException {
        when(articleDAO.createArticleAuthors(FIRST_AUTHOR_ID, THIRD_ARTICLE_ID)).thenReturn(EXPECTED_TRUE);
        boolean actual = articleDAO.createArticleAuthors(FIRST_AUTHOR_ID, THIRD_ARTICLE_ID);
        assertEquals(EXPECTED_TRUE, actual);
    }

    @Test
    public void findAllTest() throws DAOException {
        when(articleDAO.findAll()).thenReturn(expectedAllArticles);
        List<Article> actual = articleDAO.findAll();
        assertEquals(expectedAllArticles, actual);
    }

    @Test
    public void findArticlesByAuthorIdTest() throws DAOException {
        when(articleDAO.findArticlesByAuthorId(FIRST_AUTHOR_ID)).thenReturn(expectedByFirstAuthorIdArticles);
        Set<Article> actual = articleDAO.findArticlesByAuthorId(FIRST_AUTHOR_ID);
        assertEquals(expectedByFirstAuthorIdArticles, actual);
    }

    @Test
    public void findArticleByIdTest() throws DAOException {
        Optional<Article> expected = Optional.of(firstArticle);
        when(articleDAO.findArticleById(FIRST_ARTICLE_ID)).thenReturn(expected);
        Optional<Article> actual = articleDAO.findArticleById(FIRST_ARTICLE_ID);
        assertEquals(expected, actual);
    }

    @Test
    public void findArticleByLinkTest() throws DAOException {
        Optional<Article> expected = Optional.of(secondArticle);
        when(articleDAO.findArticleByLink(SECOND_ARTICLE_LINK)).thenReturn(expected);
        Optional<Article> actual = articleDAO.findArticleByLink(SECOND_ARTICLE_LINK);
        assertEquals(expected, actual);
    }

    @Test
    public void deleteTest() throws DAOException {
        when(articleDAO.delete(THIRD_ARTICLE_ID)).thenReturn(EXPECTED_TRUE);
        boolean actual = articleDAO.delete(THIRD_ARTICLE_ID);
        assertEquals(EXPECTED_TRUE, actual);
    }
}
