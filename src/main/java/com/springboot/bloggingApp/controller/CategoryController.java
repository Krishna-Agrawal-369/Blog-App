package com.springboot.bloggingApp.controller;

import com.springboot.bloggingApp.dto.CategoryDto;
import com.springboot.bloggingApp.entities.Category;
import com.springboot.bloggingApp.entities.Response;
import com.springboot.bloggingApp.entities.User;
import com.springboot.bloggingApp.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    private Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @GetMapping("/all")
    private ResponseEntity<Response> getAllCategories(@Valid @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                                      @Valid @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                      @Valid @RequestParam(value = "sortBy", defaultValue = "categoryId", required = false) String sortBy,
                                                      @Valid @RequestParam(value = "sortDir", defaultValue = "AESC", required = false) String sortDir) {
        logger.info("CategoryController :: Get all categories");
        return new ResponseEntity<>(new Response(true, 0, "All categories fetched successfully", this.categoryService.getAllCategories(pageSize, pageNumber, sortBy, sortDir)), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    private ResponseEntity<Response> getCategoryById(@PathVariable("id") int categoryId) {
        logger.info("CategoryController :: Get category with ID : {}", categoryId);
        Optional<CategoryDto> category = this.categoryService.getCategoryById(categoryId);
        return category.map(value -> new ResponseEntity<>(new Response(true, 0, "Category details with ID : " + categoryId + " fetched successfully", value), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(new Response(false, -1, "Category details with ID : " + categoryId + " not found"), HttpStatus.BAD_REQUEST));
    }

    @PostMapping("/create")
    private ResponseEntity<Response> createCategory(@Valid @RequestBody Category category) {
        logger.info("CategoryController :: Add new category");
        this.categoryService.createCategory(category);
        return new ResponseEntity<>(new Response(true, 0, "Category added successfully"), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    private ResponseEntity<Response> updateUserById(@Valid @RequestBody Category category, @Valid @PathVariable("id") int categoryId) {
        logger.info("CategoryController :: Update category details of ID : {}", categoryId);
        return this.categoryService.updateCategoryById(category, categoryId);
    }

    @DeleteMapping("/delete/{id}")
    private ResponseEntity<Response> deleteUserById(@Valid @PathVariable("id") int categoryId) {
        logger.info("CategoryController :: Delete category with ID : {}", categoryId);
        return this.categoryService.deleteCategoryById(categoryId);
    }
}
