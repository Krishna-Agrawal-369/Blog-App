package com.springboot.bloggingApp.service.impl;

import com.springboot.bloggingApp.dto.UserDto;
import com.springboot.bloggingApp.entities.Response;
import com.springboot.bloggingApp.entities.User;
import com.springboot.bloggingApp.repository.UserRepository;
import com.springboot.bloggingApp.service.UserService;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    @CachePut(cacheNames = "user")
    public List<UserDto> getAllUsers(Integer pageSize, Integer pageNumber, String sortBy, String sortDir) {
        logger.info("UserService :: Get all users list");
        Sort sort = sortDir.equalsIgnoreCase("DESC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        List<User> users = this.userRepository.findAll(pageable).getContent();
        return users.stream().map(user-> this.modelMapper.map(user,UserDto.class)).collect(Collectors.toList());
    }

    @Override
    @CachePut(cacheNames = "user", key = "#userId")
    public Optional<UserDto> getUserById(int userId) {
        logger.info("UserService :: Get user with ID : {}", userId);
        Optional<User> user = this.userRepository.findById(userId);
        UserDto userDto = user.isPresent() ? this.modelMapper.map(user,UserDto.class) : null;
        return Optional.ofNullable(userDto);
    }

    @Override
    @CachePut(cacheNames = "user")
    public void createUser(User user) {
        logger.info("UserService :: Add new user");
        user.setModifiedBy(user.getCreatedBy());
        this.userRepository.save(user);
    }

    @Override
    public ResponseEntity<Response> updateUserById(User user, int userId) {
        logger.info("UserService :: Update user of user ID : {}", userId);
        Optional<User> oldUser = this.userRepository.findById(userId);
        if (oldUser.isPresent()) {
            oldUser.get().setModifiedDate(LocalDate.now().plusDays(1));
            oldUser.get().setModifiedBy(user.getCreatedBy());
            if (Objects.nonNull(user.getUserName()))
                oldUser.get().setUserName(user.getUserName());
            if (Objects.nonNull(user.getPassword()))
                oldUser.get().setPassword(user.getPassword());
            if (Objects.nonNull(user.getEmail()))
                oldUser.get().setEmail(user.getEmail());
            this.userRepository.save(oldUser.get());
            return new ResponseEntity<>(new Response(true, 0, "User details with ID : " + userId + " updated successfully"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Response(false, -1, "User details with ID : " + userId + " not found"), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<Response> updateUserEmail(int userId, String email, String createdBy) {
        logger.info("UserService :: Update user email of user ID : {}", userId);
        Optional<User> oldUser = this.userRepository.findById(userId);
        if (oldUser.isPresent()) {
            User user = oldUser.get();
            user.setEmail(email);
            user.setModifiedDate(LocalDate.now().plusDays(1));
            user.setModifiedBy(createdBy);
            this.userRepository.save(user);
            return new ResponseEntity<>(new Response(true, 0, "User email with ID : " + userId + " updated successfully"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Response(false, -1, "User with ID : " + userId + " not found"), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @CacheEvict(cacheNames = "user", key = "#userId")
    public ResponseEntity<Response> deleteUserById(int userId) {
        logger.info("UserService :: Delete user with user ID : {}", userId);
        List<User> userList = this.userRepository.findAll();
        boolean isPresent = userList.stream().anyMatch(i -> i.getUserId() == userId);
        if (isPresent) {
            this.userRepository.deleteById(userId);
            return new ResponseEntity<>(new Response(true, 0, "User details with ID : " + userId + " deleted successfully"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Response(false, -1, "User details with ID : " + userId + " not found"), HttpStatus.BAD_REQUEST);
        }
    }
}
