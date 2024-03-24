package com.springboot.bloggingApp.service.impl;

import com.springboot.bloggingApp.dto.CategoryDto;
import com.springboot.bloggingApp.dto.PostDto;
import com.springboot.bloggingApp.dto.UserDto;
import com.springboot.bloggingApp.entities.Category;
import com.springboot.bloggingApp.entities.Post;
import com.springboot.bloggingApp.entities.Response;
import com.springboot.bloggingApp.entities.User;
import com.springboot.bloggingApp.repository.CategoryRepository;
import com.springboot.bloggingApp.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Override
    @CachePut(cacheNames = "category")
    public List<CategoryDto> getAllCategories(Integer pageSize, Integer pageNumber, String sortBy, String sortDir) {
        logger.info("CategoryService :: Get all categories");
        Sort sort = sortDir.equalsIgnoreCase("DESC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        List<Category> categories = this.categoryRepository.findAll(pageable).getContent();
        return categories.stream().map(category -> this.modelMapper.map(category, CategoryDto.class)).collect(Collectors.toList());
    }

    @Override
    @CachePut(cacheNames = "category", key = "#categoryId")
    public Optional<CategoryDto> getCategoryById(int categoryId) {
        logger.info("CategoryService :: Get category with ID : {}", categoryId);
        Optional<Category> category = this.categoryRepository.findById(categoryId);
        CategoryDto categoryDto = category.isPresent() ? this.modelMapper.map(category,CategoryDto.class) : null;
        return Optional.ofNullable(categoryDto);
    }

    @Override
    @CachePut(cacheNames = "category")
    public void createCategory(Category category) {
        logger.info("CategoryService :: Add new category");
        this.categoryRepository.save(category);
    }

    @Override
    public ResponseEntity<Response> updateCategoryById(Category category, int categoryId) {
        logger.info("CategoryService :: Update category with ID : {}", categoryId);
        Optional<Category> oldCategory = this.categoryRepository.findById(categoryId);
        if (oldCategory.isPresent()) {
            if (Objects.nonNull(category.getCategoryTitle()))
                oldCategory.get().setCategoryTitle(category.getCategoryTitle());
            if (Objects.nonNull(category.getDescription()))
                oldCategory.get().setDescription(category.getDescription());
            this.categoryRepository.save(oldCategory.get());
            return new ResponseEntity<>(new Response(true, 0, "Category details with ID : " + categoryId + " updated successfully"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Response(false, -1, "Category details with ID : " + categoryId + " not found"), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @CacheEvict(cacheNames = "category", key = "#categoryId")
    public ResponseEntity<Response> deleteCategoryById(int categoryId) {
        logger.info("CategoryService :: Delete category with ID : {}", categoryId);
        List<Category> categoryList = this.categoryRepository.findAll();
        boolean isPresent = categoryList.stream().anyMatch(i -> i.getCategoryId() == categoryId);
        if (isPresent) {
            this.categoryRepository.deleteById(categoryId);
            return new ResponseEntity<>(new Response(true, 0, "Category details with ID : " + categoryId + " deleted successfully"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Response(false, -1, "Category details with ID : " + categoryId + " not found"), HttpStatus.BAD_REQUEST);
        }
    }
}
