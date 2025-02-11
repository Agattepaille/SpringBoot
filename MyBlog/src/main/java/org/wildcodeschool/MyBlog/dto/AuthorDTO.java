package org.wildcodeschool.MyBlog.dto;

import java.util.List;

public class AuthorDTO {

    private Long id;
    private String lastname;
    private String firstname;
    private String contribution;

    private List<ArticleAuthorDTO> articleAuthorDTOs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getContribution() {
        return contribution;
    }

    public void setContribution(String contribution) {
        this.contribution = contribution;
    }

    public List<ArticleAuthorDTO> getArticleAuthorDTOs() {
        return articleAuthorDTOs;
    }

    public void setArticleAuthorDTOs(List<ArticleAuthorDTO> articleAuthorDTOs) {
        this.articleAuthorDTOs = articleAuthorDTOs;
    }
}
