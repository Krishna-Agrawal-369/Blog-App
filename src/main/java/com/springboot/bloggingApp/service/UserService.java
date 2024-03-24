package com.springboot.bloggingApp.service;

import com.springboot.bloggingApp.dto.UserDto;
import com.springboot.bloggingApp.entities.Response;
import com.springboot.bloggingApp.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

    List<UserDto> getAllUsers(Integer pageSize, Integer pageNumber, String sortBy, String sortDir);

    Optional<UserDto> getUserById(int userId);

    void createUser(User user);

    ResponseEntity<Response> updateUserById(User user, int userId);

    ResponseEntity<Response> updateUserEmail(int userId, String email, String createdBy);

    ResponseEntity<Response> deleteUserById(int userId);
}
