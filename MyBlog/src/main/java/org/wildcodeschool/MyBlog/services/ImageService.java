package org.wildcodeschool.MyBlog.services;

import org.springframework.stereotype.Component;
import org.wildcodeschool.MyBlog.dto.ImageDTO;
import org.wildcodeschool.MyBlog.exception.ResourceNotFoundException;
import org.wildcodeschool.MyBlog.mapper.ImageMapper;
import org.wildcodeschool.MyBlog.model.Article;
import org.wildcodeschool.MyBlog.model.Image;
import org.wildcodeschool.MyBlog.repository.ArticleRepository;
import org.wildcodeschool.MyBlog.repository.ImageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ImageService {
    private final ArticleRepository articleRepository;
    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;

    public ImageService(
            ArticleRepository articleRepository,
            ImageRepository imageRepository,
            ImageMapper imageMapper) {
        this.articleRepository = articleRepository;
        this.imageRepository = imageRepository;
        this.imageMapper = imageMapper;
    }

    public List<ImageDTO> getAllImages() {
        List<Image> images = imageRepository.findAll();
        return images.stream().map(imageMapper::convertToDTO).collect(Collectors.toList());
    }

    public ImageDTO getImageById(Long id) {
        Image image = imageRepository.findById(id).orElse(null);
        if (image == null) {
            return null;
        }
        return imageMapper.convertToDTO(image);
    }

    public ImageDTO createImage(Image image) {
        Image savedImage = imageRepository.save(image);
        return imageMapper.convertToDTO(savedImage);
    }

    public ImageDTO updateImage(Long id, Image imageDetails) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("L'image avec l'id " + id + " n'a pas été trouvé"));
        image.setUrl(imageDetails.getUrl());

        // Mise à jour des articles
        if (imageDetails.getArticles() != null) {
            List<Article> validArticles = new ArrayList<>();
            for (Article article : imageDetails.getArticles()) {
                if (article.getId() != null) {
                    Article existingArticle = articleRepository.findById(image.getId())
                            .orElseThrow(() -> new ResourceNotFoundException("L'article avec l'id " + article.getId() + " n'a pas été trouvé"));
                    if (existingArticle != null) {
                        validArticles.add(existingArticle);
                    }
//                    else {
//                        return null;
//                    }
                } else {
                    Article savedArticle = articleRepository.save(article);
                    validArticles.add(savedArticle);
                }
            }
            image.setArticles(validArticles);
        } else {
            image.getArticles().clear();
        }

        Image updatedImage = imageRepository.save(image);
        return imageMapper.convertToDTO(updatedImage);
    }

    public boolean deleteImage(Long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("L'image avec l'id " + id + " n'a pas été trouvé"));

        imageRepository.delete(image);
        return true;
    }
}
