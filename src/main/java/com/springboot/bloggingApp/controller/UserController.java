package com.springboot.bloggingApp.controller;

import com.springboot.bloggingApp.dto.UserDto;
import com.springboot.bloggingApp.entities.Response;
import com.springboot.bloggingApp.entities.User;
import com.springboot.bloggingApp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/getAllUsers")
    private ResponseEntity<Response> getAllUsers(@Valid @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                                 @Valid @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                 @Valid @RequestParam(value = "sortBy", defaultValue = "userId", required = false) String sortBy,
                                                 @Valid @RequestParam(value = "sortDir", defaultValue = "AESC", required = false) String sortDir) {
        logger.info("UserController :: Get all users list");
        return new ResponseEntity<>(new Response(true, 0, "All users details fetched successfully", this.userService.getAllUsers(pageSize, pageNumber, sortBy, sortDir)), HttpStatus.OK);
    }

    @GetMapping("/getUser/{id}")
    private ResponseEntity<Response> getUserById(@Valid @PathVariable("id") int userId) {
        logger.info("UserController :: Get user with ID : {}", userId);
        Optional<UserDto> user = this.userService.getUserById(userId);
        return user.map(value -> new ResponseEntity<>(new Response(true, 0, "User details with user ID : " + userId + " fetched successfully", value), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(new Response(false, -1, "User details with user ID : " + userId + " not found"), HttpStatus.BAD_REQUEST));
    }

    @PostMapping("/create")
    private ResponseEntity<Response> createUser(@Valid @RequestBody User user) {
        logger.info("UserController :: Add new user");
        this.userService.createUser(user);
        return new ResponseEntity<>(new Response(true, 0, "User created successfully"), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    private ResponseEntity<Response> updateUserById(@Valid @RequestBody User user, @Valid @PathVariable("id") int userId) {
        logger.info("UserController :: Update user details of user ID : {}", userId);
        return this.userService.updateUserById(user, userId);
    }

    @PatchMapping("/update")
    private ResponseEntity<Response> updateUserEmail(@Valid @RequestParam("email") String email, @Valid @RequestParam("id") int userId, @Valid @RequestParam("createdBy") String createdBy) {
        logger.info("UserController :: Update user email of user ID : {}", userId);
        return this.userService.updateUserEmail(userId, email, createdBy);
    }

    @DeleteMapping("/delete/{id}")
    private ResponseEntity<Response> deleteUserById(@Valid @PathVariable("id") int userId) {
        logger.info("UserController :: Delete user with user ID : {}", userId);
        return this.userService.deleteUserById(userId);
    }
}
