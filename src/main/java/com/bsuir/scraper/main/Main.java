package com.bsuir.scraper.main;

import com.bsuir.scraper.exception.ServiceException;
import com.bsuir.scraper.model.entity.Article;
import com.bsuir.scraper.model.entity.Author;
import com.bsuir.scraper.model.entity.CustomComment;
import com.bsuir.scraper.model.service.ArticleService;
import com.bsuir.scraper.model.service.AuthorService;
import com.bsuir.scraper.model.service.CommentService;
import com.bsuir.scraper.util.EntityParameter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Main {
    static Logger logger = LogManager.getLogger();
    private static final String URL = "https://www.kolesa.ru/article";
    private static final String PAGE_PATTERN = "?page=";
    private static long page = 1;
    private static long articlesCounter = 0;
    private static long commentsCounter = 0;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        try {
            int maxPages = getMaxPages();
            System.out.print("Input number of pages: ");
            int pages = Math.min(in.nextInt(), maxPages);
            long startTime = System.currentTimeMillis();
            for (; page <= pages; page++) {
                Document document = Jsoup.connect(URL + PAGE_PATTERN + page).get();
                parseArticlesOnPage(document);
            }
            long endTime = System.currentTimeMillis();
            System.out.println("Articles parsed: " + articlesCounter);
            System.out.println("Comments parsed: " + commentsCounter);
            System.out.println("Elapsed time: " + TimeUnit.MILLISECONDS.toSeconds(endTime - startTime) + " s");
        } catch (ServiceException | IOException e) {
            logger.error(e.getMessage());
        }
    }

    private static int getMaxPages() throws IOException {
        int maxPages = 0;
        Document document = Jsoup.connect(URL).get();
        Elements elements = document.getElementsByClass("container main-container");
        if (elements.size() > 0) {
            Element element = elements.get(0);
            elements = element.getElementsByClass("page-link");
            if (elements.size() > 0) {
                maxPages = Integer.parseInt(elements.get(elements.size() - 2).text());
            }
        }
        return maxPages;
    }

    private static void parseArticlesOnPage(Document page) throws IOException, ServiceException {
        for (Element el : page.getElementsByClass("container main-container").select("a.post-list-item")) {
            parseArticle(Jsoup.connect(el.attributes().get("href")).get());
            articlesCounter++;
        }
    }

    private static void parseArticle(Document article) throws ServiceException {
        ArticleService articleService = new ArticleService();
        AuthorService authorService = new AuthorService();
        CommentService commentService = new CommentService();
        Map<String, String> articleData = new HashMap<>();
        String link = getArticleLink(article);
        Optional<Article> optionalArticle = articleService.findArticleByLink(link);
        if (optionalArticle.isPresent()) {
            long articleId = optionalArticle.get().getArticleId();
            for (CustomComment comment : commentService.findCommentsByArticleId(articleId)) {
                commentService.delete(comment.getCommentId());
            }
            for (Author author : authorService.findAuthorsByArticleId(articleId)) {
                authorService.delete(author.getAuthorId());
            }
            articleService.delete(articleId);
        }
        String title = getTitle(article);
        String articleText = getArticleText(article);
        String articleDatetime = getArticleDatetime(article);
        Set<Author> authors = getArticleAuthors(article);
        articleData.put(EntityParameter.TITLE_KEY, title);
        articleData.put(EntityParameter.ARTICLE_TEXT_KEY, articleText);
        articleData.put(EntityParameter.ARTICLE_DATETIME_KEY, articleDatetime);
        articleData.put(EntityParameter.ARTICLE_LINK_KEY, link);
        articleService.create(articleData, authors);
        optionalArticle = articleService.findArticleByLink(link);
        String articleIdString = "";
        if (optionalArticle.isPresent()) {
            articleIdString = String.valueOf(optionalArticle.get().getArticleId());
        }
        parseArticleComments(article, articleIdString);
    }

    private static String getTitle(Document article) {
        String title = "";
        Elements elements = article.getElementsByClass("page-viewboard-pad post-page");
        if (elements.size() > 0) {
            Element element = elements.get(0);
            elements = element.getElementsByAttributeValue("property", "og:title");
            if (elements.size() > 0) {
                element = elements.get(0);
                Attributes attributes = element.attributes();
                if (attributes.size() > 0) {
                    title = attributes.get("content");
                }
            }
        }
        return title;
    }

    private static String getArticleText(Document article) {
        StringBuilder text = new StringBuilder();
        Elements elements = article.getElementsByClass("page-viewboard-pad post-page");
        if (elements.size() > 0) {
            Element element = elements.get(0);
            for (Element elem : element.getElementsByClass("post-lead")) {
                for (Element el : elem.getElementsByTag("p")) {
                    text.append(el.text());
                    text.append("\n");
                }
            }
            elements = element.getElementsByClass("post-content");
            for (Element elem : elements) {
                Elements elems = elem.getElementsByTag("p");
                for (Element el : elems) {
                    text.append(el.text());
                    if (!(elem.equals(elements.last()) && el.equals(elems.last()))) {
                        text.append("\n");
                    }
                }
            }
        }
        return text.toString();
    }

    private static String getArticleDatetime(Document article) {
        String datetime = "";
        Elements elements = article.getElementsByClass("page-viewboard-pad post-page");
        if (elements.size() > 0) {
            Element element = elements.get(0);
            elements = element.getElementsByAttributeValue("property", "article:modified_time");
            if (elements.size() > 0) {
                element = elements.get(0);
                Attributes attributes = element.attributes();
                if (attributes.size() > 0) {
                    datetime = attributes.get("content");
                }
            } else {
                elements = element.getElementsByAttributeValue("property", "article:published_time");
                if (elements.size() > 0) {
                    element = elements.get(0);
                    Attributes attributes = element.attributes();
                    if (attributes.size() > 0) {
                        datetime = attributes.get("content");
                    }
                }
            }
        }
        return datetime;
    }

    private static String getArticleLink(Document article) {
        String link = "";
        Elements elements = article.getElementsByClass("page-viewboard-pad post-page");
        if (elements.size() > 0) {
            Element element = elements.get(0);
            elements = element.getElementsByAttributeValue("property", "og:url");
            if (elements.size() > 0) {
                element = elements.get(0);
                Attributes attributes = element.attributes();
                if (attributes.size() > 0) {
                    link = attributes.get("content");
                }
            }
        }
        return link;
    }

    private static Set<Author> getArticleAuthors(Document article) {
        Set<Author> authors = new HashSet<>();
        Elements elements = article.getElementsByClass("page-viewboard-pad post-page");
        if (elements.size() > 0) {
            Element element = elements.get(0);
            for (Element elem : element.getElementsByClass("post-author-link")) {
                for (Element el : elem.getElementsByClass("post-author-name")) {
                    Author author = new Author();
                    author.setFullName(el.ownText());
                    author.setLink(el.attributes().get("href"));
                    author.setArticles(new HashSet<>());
                    authors.add(author);
                }
            }
        }
        return authors;
    }

    private static void parseArticleComments(Document article, String articleId) throws ServiceException {
        AuthorService authorService = new AuthorService();
        CommentService commentService = new CommentService();
        Elements elements = article.getElementsByClass("post-comments");
        if (elements.size() > 0) {
            Element element = elements.get(0);
            for (Element item : element.getElementsByClass("comment-item")) {
                Map<String, String> commentData = new HashMap<>();
                commentData.put(EntityParameter.COMMENT_TEXT_KEY, getCommentText(item));
                commentData.put(EntityParameter.COMMENT_DATETIME_KEY, getCommentDatetime(item));
                String authorLink = getAuthorLink(item);
                String authorId = "";
                Optional<Author> optionalAuthor = authorService.findAuthorByLink(authorLink);
                if (optionalAuthor.isPresent()) {
                    authorId = String.valueOf(optionalAuthor.get().getAuthorId());
                } else {
                    Map<String, String> authorData = new HashMap<>();
                    authorData.put(EntityParameter.FULL_NAME_KEY, authorLink);
                    authorData.put(EntityParameter.AUTHOR_LINK_KEY, authorLink);
                    authorService.create(authorData);
                    optionalAuthor = authorService.findAuthorByLink(authorLink);
                    if (optionalAuthor.isPresent()) {
                        authorId = String.valueOf(optionalAuthor.get().getAuthorId());
                    }
                }
                commentData.put(EntityParameter.ARTICLE_ID_KEY, articleId);
                commentData.put(EntityParameter.AUTHOR_ID_KEY, authorId);
                commentService.create(commentData);
                commentsCounter++;
            }
        }
    }

    private static String getCommentText(Element comment) {
        StringBuilder text = new StringBuilder();
        for (Element content : comment.getElementsByClass("comment-content")) {
            for (Element element : content.getElementsByTag("p")) {
                text.append(element.text());
            }
        }
        return text.toString();
    }

    private static String getCommentDatetime(Element comment) {
        String datetime = "";
        Elements elements = comment.getElementsByClass("comment-date");
        if (elements.size() > 0) {
            Element element = elements.get(0);
            datetime = element.text();
        }
        return datetime;
    }

    private static String getAuthorLink(Element comment) {
        String link = "";
        Elements elements = comment.getElementsByClass("comment-username");
        if (elements.size() > 0) {
            Element element = elements.get(0);
            link = element.text();
        }
        return link;
    }
}
