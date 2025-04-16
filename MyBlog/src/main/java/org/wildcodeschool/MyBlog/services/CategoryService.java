package org.wildcodeschool.MyBlog.services;

import org.springframework.stereotype.Service;
import org.wildcodeschool.MyBlog.dto.CategoryDTO;
import org.wildcodeschool.MyBlog.exception.ResourceNotFoundException;
import org.wildcodeschool.MyBlog.mapper.CategoryMapper;
import org.wildcodeschool.MyBlog.model.Category;
import org.wildcodeschool.MyBlog.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(
            CategoryRepository categoryRepository,
            CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(categoryMapper::convertToDTO).collect(Collectors.toList());

    }

    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La catégorie avec l'id " + id + " n'a pas été trouvé"));
        return categoryMapper.convertToDTO(category);
    }

    public CategoryDTO createCategory(Category category) {
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.convertToDTO(savedCategory);
    }

    public CategoryDTO updateCategory(Long id, Category categoryDetails) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La catégorie avec l'id " + id + " n'a pas été trouvé"));
        category.setName(categoryDetails.getName());
        Category updatedCategory = categoryRepository.save(category);

        return categoryMapper.convertToDTO(updatedCategory);
    }

    public boolean deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La catégorie avec l'id " + id + " n'a pas été trouvé"));
        categoryRepository.delete(category);
        return true;
    }
}
