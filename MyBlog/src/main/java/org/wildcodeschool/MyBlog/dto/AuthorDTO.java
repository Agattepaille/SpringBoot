package org.wildcodeschool.MyBlog.dto;

import org.wildcodeschool.MyBlog.model.ArticleAuthor;

public class AuthorDTO {

    private Long id;
    private String lastname;
    private String firstname;
    private ArticleAuthor articleAuthor;

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

    public ArticleAuthor getArticleAuthor() {
        return articleAuthor;
    }

    public void setArticleAuthor(ArticleAuthor articleAuthor) {
        this.articleAuthor = articleAuthor;
    }

}
