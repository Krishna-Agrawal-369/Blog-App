package com.springboot.bloggingApp.service;

import com.springboot.bloggingApp.dto.CommentDto;
import com.springboot.bloggingApp.entities.Comment;
import com.springboot.bloggingApp.entities.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CommentService {

    List<CommentDto> getAllComments(Integer pageSize, Integer pageNumber, String sortBy, String sortDir);
    Optional<CommentDto> getCommentById(int commentId);
    void createComment(CommentDto commentDto, int postId);
    ResponseEntity<Response> updateCommentContent(int commentId, String content);
    ResponseEntity<Response> deleteCommentById(int commentId);
}
