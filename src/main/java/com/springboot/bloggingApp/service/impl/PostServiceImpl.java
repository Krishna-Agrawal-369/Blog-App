package com.springboot.bloggingApp.service.impl;

import com.springboot.bloggingApp.dto.PostDto;
import com.springboot.bloggingApp.entities.Category;
import com.springboot.bloggingApp.entities.Post;
import com.springboot.bloggingApp.entities.Response;
import com.springboot.bloggingApp.entities.User;
import com.springboot.bloggingApp.repository.CategoryRepository;
import com.springboot.bloggingApp.repository.PostRepository;
import com.springboot.bloggingApp.repository.UserRepository;
import com.springboot.bloggingApp.service.PostService;
import org.apache.kafka.common.errors.ResourceNotFoundException;
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
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    private Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    @Override
    @CachePut(cacheNames = "post")
    public List<PostDto> getAllPosts(Integer pageSize, Integer pageNumber, String sortBy, String sortDir) {
        logger.info("PostService :: Get all posts");
        Sort sort = sortDir.equalsIgnoreCase("DESC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        List<Post> posts = this.postRepository.findAll(pageable).getContent();
        return posts.stream().map(post -> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
    }

    @Override
    @CachePut(cacheNames = "post", key = "#postId")
    public Optional<PostDto> getPostById(int postId) {
        logger.info("PostService :: Get post with ID : {}", postId);
        Optional<Post> post = this.postRepository.findById(postId);
        PostDto postDto = post.isPresent() ? this.modelMapper.map(post, PostDto.class) : null;
        return Optional.ofNullable(postDto);
    }

    @Override
    @CachePut(cacheNames = "post")
    public void createPost(PostDto postDto, int userId, int categoryId) {
        logger.info("PostService :: Add new post");
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with user ID : " + userId));
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with category ID : " + categoryId));
        Post post = this.modelMapper.map(postDto, Post.class);
        post.setUser(user);
        post.setCategory(category);
        this.postRepository.save(post);
    }

    @Override
    public ResponseEntity<Response> updatePost(Post post, int postId, int categoryId, int userId) {
        logger.info("PostService :: Update post of ID : {}", postId);
        Optional<Post> oldPost = this.postRepository.findById(postId);
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with user ID : " + userId));
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with category ID : " + categoryId));
        if (oldPost.isPresent()) {
            if (Objects.nonNull(post.getTitle()))
                oldPost.get().setTitle(post.getTitle());
            if (Objects.nonNull(post.getContent()))
                oldPost.get().setContent(post.getContent());
            if (Objects.nonNull(user))
                oldPost.get().setUser(user);
            if (Objects.nonNull(category))
                oldPost.get().setCategory(category);
            this.postRepository.save(oldPost.get());
            return new ResponseEntity<>(new Response(true, 0, "Post details with ID : " + postId + " updated successfully"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Response(false, -1, "Post details with ID : " + postId + " not found"), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @CacheEvict(cacheNames = "post", key = "#postId")
    public ResponseEntity<Response> deletePostById(int postId) {
        logger.info("PostService :: Delete post with ID : {}", postId);
        List<Post> postList = this.postRepository.findAll();
        boolean isPresent = postList.stream().anyMatch(i -> i.getPostId() == postId);
        if (isPresent) {
            this.postRepository.deleteById(postId);
            return new ResponseEntity<>(new Response(true, 0, "Post details with ID : " + postId + " deleted successfully"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Response(false, -1, "Post details with ID : " + postId + " not found"), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<Response> getPostByCategory(int categoryId) {
        logger.info("PostService :: Get post with category ID : {}", categoryId);
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with category ID : " + categoryId));
        List<Post> posts = this.postRepository.findByCategory(category);
        return !posts.isEmpty() ? new ResponseEntity<>(new Response(true, 0, "Post fetched successfully with category ID : " + categoryId, posts.stream().map(post -> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList())), HttpStatus.OK) :
                new ResponseEntity<>(new Response(false, -1, "No post found with category ID : " + categoryId), HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Response> getPostByUser(int userId) {
        logger.info("PostService :: Get post with user ID : {}", userId);
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with user ID : " + userId));
        List<Post> posts = this.postRepository.findByUser(user);
        return !posts.isEmpty() ? new ResponseEntity<>(new Response(true, 0, "Post fetched successfully with user ID : " + userId, posts.stream().map(post -> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList())), HttpStatus.OK) :
                new ResponseEntity<>(new Response(false, -1, "No post found with user ID : " + userId), HttpStatus.BAD_REQUEST);

    }

    @Override
    public ResponseEntity<Response> searchPostByTitle(String title) {
        logger.info("PostService :: Get post with title : {}", title);
        List<Post> posts = this.postRepository.findByTitleContaining(title);
        return !posts.isEmpty() ? new ResponseEntity<>(new Response(true, 0, "Post fetched successfully with title : " + title, posts.stream().map(post -> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList())), HttpStatus.OK) :
                new ResponseEntity<>(new Response(false, -1, "No post found with title : " + title), HttpStatus.BAD_REQUEST);
    }
}
