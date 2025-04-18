package org.wildcodeschool.MyBlog.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @ManyToMany(mappedBy = "images")
    private List<Article> articles;

    // Getters et setters
    public Long getId() {
        return id;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public List<Article> getArticles() { return articles; }
    public void setArticles(List<Article> articles) { this.articles = articles; }
}
