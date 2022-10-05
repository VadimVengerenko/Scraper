package com.bsuir.scraper.model.entity;

import java.util.Set;

public class Author extends CustomEntity {
    private long authorId;
    private String fullName;
    private String link;
    private Set<Article> articles;

    public Author() {

    }

    public Author(long authorId) {
        this.authorId = authorId;
    }

    public Author(String link, Set<Article> articles) {
        this.link = link;
        this.articles = articles;
    }

    public Author(long authorId, String link, Set<Article> articles) {
        this.authorId = authorId;
        this.link = link;
        this.articles = articles;
    }

    public Author(long authorId, String fullName, String link, Set<Article> articles) {
        this.authorId = authorId;
        this.fullName = fullName;
        this.link = link;
        this.articles = articles;
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Set<Article> getArticles() {
        return articles;
    }

    public void setArticles(Set<Article> articles) {
        this.articles = articles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Author author = (Author) o;
        return Long.compare(authorId, author.authorId) == 0 && fullName.equals(author.fullName)
                && link.equals(author.link) && articles.equals(author.articles);
    }

    @Override
    public int hashCode() {
        int result = 1;
        int prime = 31;
        result = prime * result + Long.hashCode(authorId);
        result = prime * result + (fullName != null ? fullName.hashCode() : 0);
        result = prime * result + (link != null ? link.hashCode() : 0);
        result = prime * result + (articles != null ? articles.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Author{");
        sb.append("authorId=").append(authorId);
        sb.append(", fullName='").append(fullName).append('\'');
        sb.append(", link='").append(link).append('\'');
        sb.append(", articles=").append(articles);
        sb.append('}');
        return sb.toString();
    }
}
