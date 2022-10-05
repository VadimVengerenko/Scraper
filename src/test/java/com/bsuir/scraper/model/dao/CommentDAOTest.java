package com.bsuir.scraper.model.dao;

import com.bsuir.scraper.exception.DAOException;
import com.bsuir.scraper.model.entity.Article;
import com.bsuir.scraper.model.entity.Author;
import com.bsuir.scraper.model.entity.CustomComment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class CommentDAOTest {
    @Mock
    private CommentDAO commentDAO;
    List<CustomComment> expectedAllComments;
    List<CustomComment> expectedBySecondArticleIdComments;
    private CustomComment newComment;
    private final boolean EXPECTED_TRUE = true;
    private final long FIRST_COMMENT_ID = 0;
    private final long SECOND_ARTICLE_ID = 2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        CustomComment firstComment = new CustomComment(FIRST_COMMENT_ID, new Article(1), new Author(0));
        CustomComment secondComment = new CustomComment(2, new Article(SECOND_ARTICLE_ID), new Author(1));
        CustomComment thirdComment = new CustomComment(3, new Article(SECOND_ARTICLE_ID), new Author(1));
        newComment = new CustomComment(new Article(), new Author());
        expectedAllComments = new ArrayList<>(Arrays.asList(firstComment, secondComment, thirdComment));
        expectedBySecondArticleIdComments = new ArrayList<>(Arrays.asList(secondComment, thirdComment));
    }

    @Test
    public void createTest() throws DAOException {
        when(commentDAO.create(newComment)).thenReturn(EXPECTED_TRUE);
        boolean actual = commentDAO.create(newComment);
        assertEquals(EXPECTED_TRUE, actual);
    }

    @Test
    public void findAllTest() throws DAOException {
        when(commentDAO.findAll()).thenReturn(expectedAllComments);
        List<CustomComment> actual = commentDAO.findAll();
        assertEquals(expectedAllComments, actual);
    }

    @Test
    public void findCommentsByArticleIdTest() throws DAOException {
        when(commentDAO.findCommentsByArticleId(SECOND_ARTICLE_ID)).thenReturn(expectedBySecondArticleIdComments);
        List<CustomComment> actual = commentDAO.findCommentsByArticleId(SECOND_ARTICLE_ID);
        assertEquals(expectedBySecondArticleIdComments, actual);
    }

    @Test
    public void deleteTest() throws DAOException {
        when(commentDAO.delete(FIRST_COMMENT_ID)).thenReturn(EXPECTED_TRUE);
        boolean actual = commentDAO.delete(FIRST_COMMENT_ID);
        assertEquals(EXPECTED_TRUE, actual);
    }
}
