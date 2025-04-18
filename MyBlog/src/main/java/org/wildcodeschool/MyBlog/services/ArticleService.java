package org.wildcodeschool.MyBlog.services;

import org.springframework.stereotype.Service;
import org.wildcodeschool.MyBlog.dto.ArticleAuthorDTO;
import org.wildcodeschool.MyBlog.dto.ArticleCreateDTO;
import org.wildcodeschool.MyBlog.dto.ArticleDTO;
import org.wildcodeschool.MyBlog.dto.ImageDTO;
import org.wildcodeschool.MyBlog.exception.ResourceNotFoundException;
import org.wildcodeschool.MyBlog.mapper.ArticleMapper;
import org.wildcodeschool.MyBlog.mapper.ImageMapper;
import org.wildcodeschool.MyBlog.model.*;
import org.wildcodeschool.MyBlog.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final AuthorRepository authorRepository;
    private final ArticleAuthorRepository articleAuthorRepository;
    private final ImageMapper imageMapper;

    public ArticleService(
            ArticleRepository articleRepository,
            ArticleMapper articleMapper,
            CategoryRepository categoryRepository,
            ImageRepository imageRepository,
            AuthorRepository authorRepository,
            ArticleAuthorRepository articleAuthorRepository,
            ImageMapper imageMapper) {
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
        this.categoryRepository = categoryRepository;
        this.imageRepository = imageRepository;
        this.authorRepository = authorRepository;
        this.articleAuthorRepository = articleAuthorRepository;
        this.imageMapper = imageMapper;
    }

    public List<ArticleDTO> getAllArticles() {
        List<Article> articles = articleRepository.findAll();
        return articles.stream().map(articleMapper::convertToDTO).collect(Collectors.toList());
    }

    public ArticleDTO getArticleById(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("L'article avec l'id " + id + " n'a pas été trouvé"));
        return articleMapper.convertToDTO(article);
    }

    public ArticleDTO createArticle(ArticleCreateDTO articleCreateDTO) {
        Article article = articleMapper.convertToEntity(articleCreateDTO);
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());

        // Gestion de la catégorie
        if (articleCreateDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(articleCreateDTO.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("La catégorie avec l'id " + article.getCategory().getId() + " n'a pas été trouvée"));
            article.setCategory(category);
        }

        // Gestion des images
        if (articleCreateDTO.getImages() != null && !articleCreateDTO.getImages().isEmpty()) {
            List<Image> validImages = new ArrayList<>();
            for (ImageDTO imageDTO : articleCreateDTO.getImages()) {
                if (imageDTO.getId() != null) {
                    Image existingImage = imageRepository.findById(imageDTO.getId()).orElse(null);
                    if (existingImage != null) {
                        validImages.add(existingImage);
                    } else {
                        return null;
                    }
                } else {
                    Image image = imageMapper.convertToEntity(imageDTO);
                    Image savedImage = imageRepository.save(image);
                    validImages.add(savedImage);
                }
            }
            article.setImages(validImages);
        }

        Article savedArticle = articleRepository.save(article);

        // Gestion des auteurs
        if (articleCreateDTO.getAuthors() != null) {
            for (ArticleAuthorDTO articleAuthorDTO : articleCreateDTO.getAuthors()) {
                Long authorId = articleAuthorDTO.getAuthorId();
                Author author = authorRepository.findById(authorId)
                        .orElseThrow(() -> new ResourceNotFoundException("L'auteur avec l'id " + articleAuthorDTO.getAuthorId() + " n'a pas été trouvé"));

                ArticleAuthor articleAuthor = new ArticleAuthor();
                articleAuthor.setAuthor(author);
                articleAuthor.setArticle(savedArticle);
                articleAuthor.setContribution(articleAuthorDTO.getContribution());

                articleAuthorRepository.save(articleAuthor);
            }
        }

        return articleMapper.convertToDTO(savedArticle);
    }

    public ArticleDTO updateArticle(Long id, Article articleDetails) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("L'article avec l'id " + id + " n'a pas été trouvé"));
        article.setTitle(articleDetails.getTitle());
        article.setContent(articleDetails.getContent());
        article.setUpdatedAt(LocalDateTime.now());

        // Mise à jour de la catégorie
        if (articleDetails.getCategory() != null) {
            Category category = categoryRepository.findById(articleDetails.getCategory().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("La catégorie avec l'id " + articleDetails.getCategory().getId() + " n'a pas été trouvée"));
            article.setCategory(category);
        }

        // Mise à jour des images
        if (articleDetails.getImages() != null) {
            processImages(articleDetails, article);
        } else {
            article.getImages().clear();
        }

        // Mise à jour des auteurs
        if (articleDetails.getArticleAuthors() != null) {
            for (ArticleAuthor oldArticleAuthor : article.getArticleAuthors()) {
                articleAuthorRepository.delete(oldArticleAuthor);
            }

            List<ArticleAuthor> updatedArticleAuthors = new ArrayList<>();

            for (ArticleAuthor articleAuthorDetails : articleDetails.getArticleAuthors()) {
                Author author = articleAuthorDetails.getAuthor();
                author = authorRepository.findById(author.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("L'auteur avec l'id " + articleAuthorDetails.getAuthor().getId() + " n'a pas été trouvé"));

                ArticleAuthor newArticleAuthor = new ArticleAuthor();
                newArticleAuthor.setAuthor(author);
                newArticleAuthor.setArticle(article);
                newArticleAuthor.setContribution(articleAuthorDetails.getContribution());

                updatedArticleAuthors.add(newArticleAuthor);
            }

            for (ArticleAuthor articleAuthor : updatedArticleAuthors) {
                articleAuthorRepository.save(articleAuthor);
            }

            article.setArticleAuthors(updatedArticleAuthors);
        }

        Article updatedArticle = articleRepository.save(article);
        return articleMapper.convertToDTO(updatedArticle);
    }

    public boolean deleteArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("L'article avec l'id " + id + " n'a pas été trouvé"));
        articleAuthorRepository.deleteAll(article.getArticleAuthors());
        articleRepository.delete(article);
        return true;
    }

    private void processImages(Article articleDetails, Article article) {
        List<Image> validImages = new ArrayList<>();
        for (Image image : articleDetails.getImages()) {
            if (image.getId() != null) {
                Image existingImage = imageRepository.findById(image.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("L'image avec l'id " + image.getId() + " n'a pas été trouvée"));
                if (existingImage != null) {
                    validImages.add(existingImage);
                }
            } else {

                Image savedImage = imageRepository.save(image);
                validImages.add(savedImage);
            }
        }
        article.setImages(validImages);
    }

}
