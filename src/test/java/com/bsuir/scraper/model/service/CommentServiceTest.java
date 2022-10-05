package com.bsuir.scraper.model.service;

import com.bsuir.scraper.exception.ServiceException;
import com.bsuir.scraper.model.entity.Article;
import com.bsuir.scraper.model.entity.Author;
import com.bsuir.scraper.model.entity.CustomComment;
import com.bsuir.scraper.util.EntityParameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class CommentServiceTest {
    @Mock
    private CommentService commentService;
    private List<CustomComment> expectedAllComments;
    private List<CustomComment> expectedByThirdArticleIdComments;
    private Map<String, String> newCommentData;
    private final boolean EXPECTED_TRUE = true;
    private final long SECOND_COMMENT_ID = 2;
    private final long THIRD_ARTICLE_ID = 3;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        CustomComment firstComment = new CustomComment(1, new Article(1), new Author(1));
        CustomComment secondComment = new CustomComment(SECOND_COMMENT_ID, new Article(THIRD_ARTICLE_ID), new Author(2));
        CustomComment thirdComment = new CustomComment(3, new Article(THIRD_ARTICLE_ID), new Author(3));
        newCommentData = new HashMap<>() {{
            put(EntityParameter.ARTICLE_ID_KEY, "4");
            put(EntityParameter.AUTHOR_ID_KEY, "5");
        }};
        expectedAllComments = new ArrayList<>(Arrays.asList(firstComment, secondComment, thirdComment));
        expectedByThirdArticleIdComments = new ArrayList<>(Arrays.asList(secondComment, thirdComment));
    }

    @Test
    public void createTest() throws ServiceException {
        when(commentService.create(newCommentData)).thenReturn(EXPECTED_TRUE);
        boolean actual = commentService.create(newCommentData);
        assertEquals(EXPECTED_TRUE, actual);
    }

    @Test
    public void findAllTest() throws ServiceException {
        when(commentService.findAll()).thenReturn(expectedAllComments);
        List<CustomComment> actual = commentService.findAll();
        assertEquals(expectedAllComments, actual);
    }

    @Test
    public void findCommentsByArticleIdTest() throws ServiceException {
        when(commentService.findCommentsByArticleId(THIRD_ARTICLE_ID)).thenReturn(expectedByThirdArticleIdComments);
        List<CustomComment> actual = commentService.findCommentsByArticleId(THIRD_ARTICLE_ID);
        assertEquals(expectedByThirdArticleIdComments, actual);
    }

    @Test
    public void deleteTest() throws ServiceException {
        when(commentService.delete(SECOND_COMMENT_ID)).thenReturn(EXPECTED_TRUE);
        boolean actual = commentService.delete(SECOND_COMMENT_ID);
        assertEquals(EXPECTED_TRUE, actual);
    }
}
