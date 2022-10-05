package com.bsuir.scraper.model.entity;

import java.time.LocalDate;
import java.util.Set;

public class Article extends CustomEntity {
    private long articleId;
    private String title;
    private String text;
    private LocalDate datetime;
    private String link;
    private Set<Author> authors;

    public Article() {

    }

    public Article(long articleId) {
        this.articleId = articleId;
    }

    public Article(String link, Set<Author> authors) {
        this.link = link;
        this.authors = authors;
    }

    public Article(long articleId, String link, Set<Author> authors) {
        this.articleId = articleId;
        this.link = link;
        this.authors = authors;
    }

    public Article(long articleId, String title, String text, LocalDate datetime, String link, Set<Author> authors) {
        this.articleId = articleId;
        this.title = title;
        this.text = text;
        this.datetime = datetime;
        this.link = link;
        this.authors = authors;
    }

    public long getArticleId() {
        return articleId;
    }

    public void setArticleId(long articleId) {
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDate getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDate datetime) {
        this.datetime = datetime;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Article article = (Article) o;
        return Long.compare(articleId, article.articleId) == 0 && title.equals(article.title)
                && text.equals(article.text) && datetime.equals(article.datetime)
                && link.equals(article.link) && authors.equals(article.authors);
    }

    @Override
    public int hashCode() {
        int result = 1;
        int prime = 31;
        result = prime * result + Long.hashCode(articleId);
        result = prime * result + (title != null ? title.hashCode() : 0);
        result = prime * result + (text != null ? text.hashCode() : 0);
        result = prime * result + (datetime != null ? datetime.hashCode() : 0);
        result = prime * result + (link != null ? link.hashCode() : 0);
        result = prime * result + (authors != null ? authors.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Article{");
        sb.append("articleId=").append(articleId);
        sb.append(", title='").append(title).append('\'');
        sb.append(", text='").append(text).append('\'');
        sb.append(", datetime=").append(datetime);
        sb.append(", link='").append(link).append('\'');
        sb.append(", authors=").append(authors);
        sb.append('}');
        return sb.toString();
    }
}
