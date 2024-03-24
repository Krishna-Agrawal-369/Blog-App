package com.springboot.bloggingApp.service.impl;

import com.springboot.bloggingApp.dto.CommentDto;
import com.springboot.bloggingApp.entities.*;
import com.springboot.bloggingApp.repository.CommentRepository;
import com.springboot.bloggingApp.repository.PostRepository;
import com.springboot.bloggingApp.service.CommentService;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ModelMapper modelMapper;

    private Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    @Override
    @CachePut(cacheNames = "comment")
    public List<CommentDto> getAllComments(Integer pageSize, Integer pageNumber, String sortBy, String sortDir) {
        logger.info("CommentService :: Get all comments");
        Sort sort = sortDir.equalsIgnoreCase("DESC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        List<Comment> comments = this.commentRepository.findAll(pageable).getContent();
        return comments.stream().map(comment -> this.modelMapper.map(comment, CommentDto.class)).collect(Collectors.toList());
    }

    @Override
    @CachePut(cacheNames = "comment", key = "#commentId")
    public Optional<CommentDto> getCommentById(int commentId) {
        logger.info("CommentService :: Get comment with ID : {}", commentId);
        Optional<Comment> comment = this.commentRepository.findById(commentId);
        CommentDto commentDto = comment.isPresent() ? this.modelMapper.map(comment,CommentDto.class) : null;
        return Optional.ofNullable(commentDto);
    }

    @Override
    @CachePut(cacheNames = "comment")
    public void createComment(CommentDto commentDto, int postId) {
        logger.info("CommentService :: Add new comment");
        Post post = this.postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found with category ID : " + postId));
        Comment comment = this.modelMapper.map(commentDto,Comment.class);
        comment.setPost(post);
        this.commentRepository.save(comment);
    }

    @Override
    public ResponseEntity<Response> updateCommentContent(int commentId, String content) {
        logger.info("CommentService :: Update comment content of comment ID : {}", commentId);
        Optional<Comment> oldComment = this.commentRepository.findById(commentId);
        if (oldComment.isPresent()) {
            Comment comment = oldComment.get();
            comment.setContent(content);
            this.commentRepository.save(comment);
            return new ResponseEntity<>(new Response(true, 0, "Comment content with ID : " + commentId + " updated successfully"), HttpStatus.OK);
        } else
            return new ResponseEntity<>(new Response(false, -1, "Comment with ID : " + commentId + " not found"), HttpStatus.BAD_REQUEST);
    }

    @Override
    @CacheEvict(cacheNames = "comment", key = "#commentId")
    public ResponseEntity<Response> deleteCommentById(int commentId) {
        logger.info("CommentService :: Delete comment with ID : {}", commentId);
        List<Comment> comments = this.commentRepository.findAll();
        boolean isPresent = comments.stream().anyMatch(i -> i.getCommentId() == commentId);
        if (isPresent) {
            this.commentRepository.deleteById(commentId);
            return new ResponseEntity<>(new Response(true, 0, "Comment details with ID : " + commentId + " deleted successfully"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Response(false, -1, "Comment details with ID : " + commentId + " not found"), HttpStatus.BAD_REQUEST);
        }
    }
}
