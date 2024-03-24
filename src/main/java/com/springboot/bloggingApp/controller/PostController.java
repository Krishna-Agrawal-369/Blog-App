package com.springboot.bloggingApp.controller;

import com.springboot.bloggingApp.dto.PostDto;
import com.springboot.bloggingApp.entities.Post;
import com.springboot.bloggingApp.entities.Response;
import com.springboot.bloggingApp.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    private Logger logger = LoggerFactory.getLogger(PostController.class);

    @GetMapping("/all")
    private ResponseEntity<Response> getAllPosts(@Valid @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                                 @Valid @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                 @Valid @RequestParam(value = "sortBy", defaultValue = "postId", required = false) String sortBy,
                                                 @Valid @RequestParam(value = "sortDir", defaultValue = "AESC", required = false) String sortDir) {
        logger.info("PostController :: Get all posts");
        return new ResponseEntity<>(new Response(true, 0, "All posts fetched successfully", this.postService.getAllPosts(pageSize, pageNumber, sortBy, sortDir)), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    private ResponseEntity<Response> getPostById(@Valid @PathVariable("id") int postId) {
        logger.info("PostController :: Get post with ID : {}", postId);
        Optional<PostDto> post = this.postService.getPostById(postId);
        return post.map(value -> new ResponseEntity<>(new Response(true, 0, "Post details with ID : " + postId + " fetched successfully", value), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(new Response(false, -1, "Post details with ID : " + postId + " not found"), HttpStatus.BAD_REQUEST));
    }

    @PostMapping("/create")
    private ResponseEntity<Response> createPost(@Valid @RequestBody PostDto postDto, @Valid @RequestParam("user") int userId, @Valid @RequestParam("category") int categoryId) {
        logger.info("PostController :: Add new post");
        this.postService.createPost(postDto, userId, categoryId);
        return new ResponseEntity<>(new Response(true, 0, "Post added successfully"), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    private ResponseEntity<Response> updatePost(@Valid @RequestBody Post post, @Valid @PathVariable("id") int postId, @Valid @RequestParam("user") int userId, @Valid @RequestParam("category") int categoryId) {
        logger.info("PostController :: Update post with ID : {}", postId);
        return this.postService.updatePost(post, postId, categoryId, userId);
    }

    @DeleteMapping("/delete/{id}")
    private ResponseEntity<Response> deletePostById(@PathVariable("id") int postId) {
        logger.info("PostController :: Delete post with ID : {}", postId);
        return this.postService.deletePostById(postId);
    }

    @GetMapping("/postByCategory/{id}")
    private ResponseEntity<Response> getPostByCategoryId(@PathVariable("id") int categoryId) {
        logger.info("PostController :: Get post with category ID : {}", categoryId);
        return this.postService.getPostByCategory(categoryId);
    }

    @GetMapping("/postByUser/{id}")
    private ResponseEntity<Response> getPostByUserId(@PathVariable("id") int userId) {
        logger.info("PostController :: Get post with user ID : {}", userId);
        return this.postService.getPostByUser(userId);
    }

    @GetMapping("/postByTitle/{title}")
    private ResponseEntity<Response> getPostByTitle(@PathVariable("title") String title) {
        logger.info("PostController :: Get post with title : {}", title);
        return this.postService.searchPostByTitle(title);
    }

}
