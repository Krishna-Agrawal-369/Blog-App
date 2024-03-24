package com.springboot.bloggingApp.service;

import com.springboot.bloggingApp.dto.PostDto;
import com.springboot.bloggingApp.entities.Category;
import com.springboot.bloggingApp.entities.Post;
import com.springboot.bloggingApp.entities.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface PostService {

    List<PostDto> getAllPosts(Integer pageSize, Integer pageNumber, String sortBy, String sortDir);

    Optional<PostDto> getPostById(int postId);

    void createPost(PostDto postDto, int userId, int categoryId);

    ResponseEntity<Response> updatePost(Post post, int postId, int categoryId, int userId);

    ResponseEntity<Response> deletePostById(int postId);

    ResponseEntity<Response> getPostByCategory(int categoryId);

    ResponseEntity<Response> getPostByUser(int userId);

    ResponseEntity<Response> searchPostByTitle(String title);
}
