package org.wildcodeschool.MyBlog.model;

import jakarta.persistence.*;

import java.util.List;


@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "category")
    private List<Article> articles;

    // Getters et setters

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Article> getArticles() { return articles; }

    public void setArticle(Article article) { this.articles = articles; }

}