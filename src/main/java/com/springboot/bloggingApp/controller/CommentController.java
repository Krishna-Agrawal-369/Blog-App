package com.springboot.bloggingApp.controller;

import com.springboot.bloggingApp.dto.CommentDto;
import com.springboot.bloggingApp.entities.Response;
import com.springboot.bloggingApp.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    private Logger logger = LoggerFactory.getLogger(CommentController.class);

    @GetMapping("/all")
    private ResponseEntity<Response> getAllComments(@Valid @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                                    @Valid @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                    @Valid @RequestParam(value = "sortBy", defaultValue = "commentId", required = false) String sortBy,
                                                    @Valid @RequestParam(value = "sortDir", defaultValue = "AESC", required = false) String sortDir) {
        logger.info("CommentController :: Get all comments");
        return new ResponseEntity<>(new Response(true, 0, "All comments fetched successfully", this.commentService.getAllComments(pageSize, pageNumber, sortBy, sortDir)), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    private ResponseEntity<Response> getCommentById(@Valid @PathVariable("id") int commentId) {
        logger.info("CommentController :: Get comment with ID : {}", commentId);
        Optional<CommentDto> comment = this.commentService.getCommentById(commentId);
        return comment.map(value -> new ResponseEntity<>(new Response(true, 0, "Comment details with ID : " + commentId + " fetched successfully", value), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(new Response(false, -1, "Comment details with ID : " + commentId + " not found"), HttpStatus.BAD_REQUEST));
    }

    @PostMapping("/create/{id}")
    private ResponseEntity<Response> createComments(@Valid @RequestBody CommentDto commentDto, @Valid @PathVariable("id") int postId) {
        logger.info("CommentController :: Add new comment");
        this.commentService.createComment(commentDto, postId);
        return new ResponseEntity<>(new Response(true, 0, "Comment added successfully"), HttpStatus.CREATED);
    }

    @PatchMapping("/update/{id}")
    private ResponseEntity<Response> updateCommentContent(@Valid @PathVariable("id") int commentId, @RequestParam("content") String content) {
        logger.info("CommentController :: Update comment with ID : {}", commentId);
        return this.commentService.updateCommentContent(commentId, content);
    }

    @DeleteMapping("/delete/{id}")
    private ResponseEntity<Response> deleteCommentById(@Valid @PathVariable("id") int commentId) {
        logger.info("CommentController :: Delete comment with ID : {}", commentId);
        return this.commentService.deleteCommentById(commentId);
    }

}
