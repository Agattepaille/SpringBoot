package org.wildcodeschool.MyBlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wildcodeschool.MyBlog.model.Article;

import java.time.LocalDateTime;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByTitle(String title);

    List<Article> findArticlesByContentContaining(String content);

    List<Article> findArticlesByCreatedAtAfter(LocalDateTime createdAt);

    List<Article> findTop5ByOrderByCreatedAtDesc();



}
