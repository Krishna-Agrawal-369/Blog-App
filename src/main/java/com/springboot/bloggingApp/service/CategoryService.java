package com.springboot.bloggingApp.service;

import com.springboot.bloggingApp.dto.CategoryDto;
import com.springboot.bloggingApp.entities.Category;
import com.springboot.bloggingApp.entities.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CategoryService {

    List<CategoryDto> getAllCategories(Integer pageSize, Integer pageNumber, String sortBy, String sortDir);

    Optional<CategoryDto> getCategoryById(int categoryId);

    void createCategory(Category category);

    ResponseEntity<Response> updateCategoryById(Category category, int categoryId);

    ResponseEntity<Response> deleteCategoryById(int categoryId);

}
