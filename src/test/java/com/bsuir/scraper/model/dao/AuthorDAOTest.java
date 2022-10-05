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

public class AuthorDAOTest {
    @Mock
    private AuthorDAO authorDAO;
    private List<Author> expectedAllAuthors;
    private Set<Author> expectedByFirstArticleIdAuthors;
    private Author firstAuthor;
    private Author thirdAuthor;
    private Author newAuthor;
    private final boolean EXPECTED_TRUE = true;
    private final long FIRST_AUTHOR_ID = 1;
    private final long SECOND_AUTHOR_ID = 2;
    private final String THIRD_AUTHOR_LINK = "thirdAuthorLink";
    private final long FIRST_ARTICLE_ID = 0;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        Set<Article> firstAuthorArticles = new HashSet<>(Arrays.asList(new Article(FIRST_ARTICLE_ID), new Article(2)));
        Set<Article> secondAuthorArticles = new HashSet<>(Arrays.asList(new Article(2), new Article(3),
                new Article(4), new Article(5)));
        Set<Article> thirdAuthorArticles = new HashSet<>(Arrays.asList(new Article(FIRST_ARTICLE_ID), new Article(6),
                new Article(7)));
        firstAuthor = new Author(FIRST_AUTHOR_ID, "firstAuthorLink", firstAuthorArticles);
        Author secondAuthor = new Author(SECOND_AUTHOR_ID, "secondAuthorLink", secondAuthorArticles);
        thirdAuthor = new Author(3, THIRD_AUTHOR_LINK, thirdAuthorArticles);
        newAuthor = new Author("newAuthorLink", new HashSet<>(Collections.singletonList(new Article(8))));
        expectedAllAuthors = new ArrayList<>(Arrays.asList(firstAuthor, secondAuthor, thirdAuthor));
        expectedByFirstArticleIdAuthors = new HashSet<>(Arrays.asList(firstAuthor, thirdAuthor));
    }

    @Test
    public void createTest() throws DAOException {
        when(authorDAO.create(newAuthor)).thenReturn(EXPECTED_TRUE);
        boolean actual = authorDAO.create(newAuthor);
        assertEquals(EXPECTED_TRUE, actual);
    }

    @Test
    public void findAllTest() throws DAOException {
        when(authorDAO.findAll()).thenReturn(expectedAllAuthors);
        List<Author> actual = authorDAO.findAll();
        assertEquals(expectedAllAuthors, actual);
    }

    @Test
    public void findAuthorsByArticleIdTest() throws DAOException {
        when(authorDAO.findAuthorsByArticleId(FIRST_ARTICLE_ID)).thenReturn(expectedByFirstArticleIdAuthors);
        Set<Author> actual = authorDAO.findAuthorsByArticleId(FIRST_ARTICLE_ID);
        assertEquals(expectedByFirstArticleIdAuthors, actual);
    }

    @Test
    public void findAuthorByLinkTest() throws DAOException {
        Optional<Author> expected = Optional.of(thirdAuthor);
        when(authorDAO.findAuthorByLink(THIRD_AUTHOR_LINK)).thenReturn(expected);
        Optional<Author> actual = authorDAO.findAuthorByLink(THIRD_AUTHOR_LINK);
        assertEquals(expected, actual);
    }

    @Test
    public void findAuthorByIdTest() throws DAOException {
        Optional<Author> expected = Optional.of(firstAuthor);
        when(authorDAO.findAuthorById(FIRST_AUTHOR_ID)).thenReturn(expected);
        Optional<Author> actual = authorDAO.findAuthorById(FIRST_AUTHOR_ID);
        assertEquals(expected, actual);
    }

    @Test
    public void deleteTest() throws DAOException {
        when(authorDAO.delete(SECOND_AUTHOR_ID)).thenReturn(EXPECTED_TRUE);
        boolean actual = authorDAO.delete(SECOND_AUTHOR_ID);
        assertEquals(EXPECTED_TRUE, actual);
    }
}
