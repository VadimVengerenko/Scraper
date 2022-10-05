package com.bsuir.scraper.model.service;

import com.bsuir.scraper.exception.ServiceException;
import com.bsuir.scraper.model.entity.Article;
import com.bsuir.scraper.model.entity.Author;
import com.bsuir.scraper.util.EntityParameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class AuthorServiceTest {
    @Mock
    private AuthorService authorService;
    private List<Author> expectedAllAuthors;
    private Set<Author> expectedByThirdArticleIdAuthors;
    private Author firstAuthor;
    private Map<String, String> newAuthorData;
    private final boolean EXPECTED_TRUE = true;
    private final String FIRST_AUTHOR_LINK = "firstAuthorLink";
    private final long SECOND_AUTHOR_ID = 2;
    private final long THIRD_ARTICLE_ID = 3;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        Set<Article> firstAuthorArticles = new HashSet<>(Arrays.asList(new Article(1), new Article(2), new Article(4)));
        Set<Article> secondAuthorArticles = new HashSet<>(Arrays.asList(new Article(), new Article(1)));
        Set<Article> thirdAuthorArticles = new HashSet<>(Collections.singletonList(new Article(THIRD_ARTICLE_ID)));
        firstAuthor = new Author(1, FIRST_AUTHOR_LINK, firstAuthorArticles);
        Author secondAuthor = new Author(SECOND_AUTHOR_ID, "secondAuthorLink", secondAuthorArticles);
        Author thirdAuthor = new Author(3, "thirdAuthorLink", thirdAuthorArticles);
        newAuthorData = new HashMap<>() {{
            put(EntityParameter.FULL_NAME_KEY, "newAuthorFullName");
            put(EntityParameter.AUTHOR_LINK_KEY, "newAuthorLink");
        }};
        expectedAllAuthors = new ArrayList<>(Arrays.asList(firstAuthor, secondAuthor, thirdAuthor));
        expectedByThirdArticleIdAuthors = new HashSet<>(Collections.singletonList(thirdAuthor));
    }

    @Test
    public void createTest() throws ServiceException {
        when(authorService.create(newAuthorData)).thenReturn(EXPECTED_TRUE);
        boolean actual = authorService.create(newAuthorData);
        assertEquals(EXPECTED_TRUE, actual);
    }

    @Test
    public void findAllTest() throws ServiceException {
        when(authorService.findAll()).thenReturn(expectedAllAuthors);
        List<Author> actual = authorService.findAll();
        assertEquals(expectedAllAuthors, actual);
    }

    @Test
    public void findAuthorByLinkTest() throws ServiceException {
        Optional<Author> expected = Optional.of(firstAuthor);
        when(authorService.findAuthorByLink(FIRST_AUTHOR_LINK)).thenReturn(expected);
        Optional<Author> actual = authorService.findAuthorByLink(FIRST_AUTHOR_LINK);
        assertEquals(expected, actual);
    }

    @Test
    public void findAuthorsByArticleIdTest() throws ServiceException {
        when(authorService.findAuthorsByArticleId(THIRD_ARTICLE_ID)).thenReturn(expectedByThirdArticleIdAuthors);
        Set<Author> actual = authorService.findAuthorsByArticleId(THIRD_ARTICLE_ID);
        assertEquals(expectedByThirdArticleIdAuthors, actual);
    }

    @Test
    public void deleteTest() throws ServiceException {
        when(authorService.delete(SECOND_AUTHOR_ID)).thenReturn(EXPECTED_TRUE);
        boolean actual = authorService.delete(SECOND_AUTHOR_ID);
        assertEquals(EXPECTED_TRUE, actual);
    }
}
