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

public class ArticleServiceTest {
    @Mock
    private ArticleService articleService;
    private List<Article> expectedArticles;
    private Article secondArticle;
    private Map<String, String> newArticleData;
    private Set<Author> newArticleAuthors;
    private final boolean EXPECTED_TRUE = true;
    private final long FIRST_ARTICLE_ID = 1;
    private final String SECOND_ARTICLE_LINK = "secondArticleLink";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        Set<Author> firstArticleAuthors = new HashSet<>(Arrays.asList(new Author(1), new Author(2)));
        Set<Author> secondArticleAuthors = new HashSet<>(Collections.singletonList(new Author(3)));
        Set<Author> thirdArticleAuthors = new HashSet<>(Arrays.asList(new Author(3), new Author(4)));
        Article firstArticle = new Article(FIRST_ARTICLE_ID, "firstArticleLink", firstArticleAuthors);
        secondArticle = new Article(2, SECOND_ARTICLE_LINK, secondArticleAuthors);
        Article thirdArticle = new Article(3, "thirdArticleLink", thirdArticleAuthors);
        newArticleData = new HashMap<>() {{
            put(EntityParameter.TITLE_KEY, "title");
            put(EntityParameter.ARTICLE_TEXT_KEY, "articleText");
            put(EntityParameter.ARTICLE_DATETIME_KEY, "2011-12-03T10:15:30+01:00");
            put(EntityParameter.ARTICLE_LINK_KEY, "articleLink");
        }};
        newArticleAuthors = new HashSet<>(Arrays.asList(new Author(5), new Author(6)));
        expectedArticles = new ArrayList<>(Arrays.asList(firstArticle, secondArticle, thirdArticle));
    }

    @Test
    public void createTest() throws ServiceException {
        when(articleService.create(newArticleData, newArticleAuthors)).thenReturn(EXPECTED_TRUE);
        boolean actual = articleService.create(newArticleData, newArticleAuthors);
        assertEquals(EXPECTED_TRUE, actual);
    }

    @Test
    public void findAllTest() throws ServiceException {
        when(articleService.findAll()).thenReturn(expectedArticles);
        List<Article> actual = articleService.findAll();
        assertEquals(expectedArticles, actual);
    }

    @Test
    public void findArticleByLinkTest() throws ServiceException {
        Optional<Article> expected = Optional.of(secondArticle);
        when(articleService.findArticleByLink(SECOND_ARTICLE_LINK)).thenReturn(expected);
        Optional<Article> actual = articleService.findArticleByLink(SECOND_ARTICLE_LINK);
        assertEquals(expected, actual);
    }

    @Test
    public void deleteTest() throws ServiceException {
        when(articleService.delete(FIRST_ARTICLE_ID)).thenReturn(EXPECTED_TRUE);
        boolean actual = articleService.delete(FIRST_ARTICLE_ID);
        assertEquals(EXPECTED_TRUE, actual);
    }
}
