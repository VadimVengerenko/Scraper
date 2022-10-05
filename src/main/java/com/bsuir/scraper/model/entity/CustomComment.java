package com.bsuir.scraper.model.entity;

import java.time.LocalDate;

public class CustomComment extends CustomEntity {
    private long commentId;
    private String text;
    private LocalDate datetime;
    private Article article;
    private Author author;

    public CustomComment() {

    }

    public CustomComment(Article articleId, Author author) {
        this.article = articleId;
        this.author = author;
    }

    public CustomComment(long commentId, Article articleId, Author author) {
        this.commentId = commentId;
        this.article = articleId;
        this.author = author;
    }

    public CustomComment(long commentId, String text, LocalDate datetime, Article articleId, Author author) {
        this.commentId = commentId;
        this.text = text;
        this.datetime = datetime;
        this.article = articleId;
        this.author = author;
    }

    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
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

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CustomComment comment = (CustomComment) o;
        return Long.compare(commentId, comment.commentId) == 0 && text.equals(comment.text)
                && datetime.equals(comment.datetime) && article.equals(comment.article)
                && author.equals(comment.author);
    }

    @Override
    public int hashCode() {
        int result = 1;
        int prime = 31;
        result = prime * result + Long.hashCode(commentId);
        result = prime * result + (text != null ? text.hashCode() : 0);
        result = prime * result + (datetime != null ? datetime.hashCode() : 0);
        result = prime * result + (article != null ? article.hashCode() : 0);
        result = prime * result + (author != null ? author.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CustomComment{");
        sb.append("commentId=").append(commentId);
        sb.append(", text='").append(text).append('\'');
        sb.append(", datetime=").append(datetime);
        sb.append(", article=").append(article);
        sb.append(", author=").append(author);
        sb.append('}');
        return sb.toString();
    }
}
