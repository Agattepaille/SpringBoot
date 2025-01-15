package org.wildcodeschool.MyBlog.dto;

import java.util.List;

public class ImageDTO {
    private Long id;
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
