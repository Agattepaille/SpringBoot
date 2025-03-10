package org.wildcodeschool.MyBlog.dto;

import org.hibernate.validator.constraints.URL;

import java.util.List;

public class ImageDTO {
    private Long id;

    @URL(message = "L'URL de l'image doit être valide")
    private String url;

    private List<ArticleDTO> articles;

    // Getters et setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<ArticleDTO> getArticles() {
        return articles;
    }

    public void setArticleIds(List<Long> articleIds) {
        this.articles = articles;
    }
}
