package org.wildcodeschool.MyBlog.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.wildcodeschool.MyBlog.dto.CategoryDTO;
import org.wildcodeschool.MyBlog.exception.ResourceNotFoundException;
import org.wildcodeschool.MyBlog.security.CustomUserDetailsService;
import org.wildcodeschool.MyBlog.security.JwtService;
import org.wildcodeschool.MyBlog.services.CategoryService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void testGetAllCategories() throws Exception {
        // Arrange
        CategoryDTO dto1 = new CategoryDTO();
        dto1.setName("Category 1");

        CategoryDTO dto2 = new CategoryDTO();
        dto2.setName("Category 2");

        when(categoryService.getAllCategories()).thenReturn(List.of(dto1, dto2));

        // Act & Assert
        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Category 1"))
                .andExpect(jsonPath("$[1].name").value("Category 2"));
    }

    @Test
    void testGetCategoryById_CategoryExists() throws Exception {
        // Arrange
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("Category 1");

        when(categoryService.getCategoryById(1L)).thenReturn(categoryDTO);

        // Act & Assert
        mockMvc.perform(get("/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Category 1"));
    }

    @Test
    void testGetCategoryById_CategoryNotFound() throws Exception {
        // Arrange
        when(categoryService.getCategoryById(99L)).thenThrow(new ResourceNotFoundException("Category not found"));

        // Act & Assert
        mockMvc.perform(get("/categories/99"))
                .andExpect(status().isNotFound());
    }
}
