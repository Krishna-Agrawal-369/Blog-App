package com.springboot.bloggingApp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.bloggingApp.entities.Customer;
import com.springboot.bloggingApp.entities.Response;
import com.springboot.bloggingApp.kafka.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Arrays;

@RestController
public class BlogController {

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private ObjectMapper mapper;

    private Logger logger = LoggerFactory.getLogger(BlogController.class);

    @PostMapping("/user/name")
    public ResponseEntity<Response> produceMessage(@RequestParam String userName) {
        return new ResponseEntity<>(new Response(true, 0, kafkaProducer.showUser(userName)), HttpStatus.OK);
    }

    @PostMapping("/upload/files")
    private ResponseEntity<Response> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        this.logger.info("Number of files uploaded : {}", files.length);
        Arrays.stream(files).forEach(file -> {
            logger.info("File Name : {}", file.getOriginalFilename());
            logger.info("File Type : {}", file.getContentType());
        });
        return new ResponseEntity<>(new Response(true, 0, "Files uploaded successfully"), HttpStatus.OK);
    }

    @PostMapping("/upload/files/json")
    private ResponseEntity<Response> uploadFilesAndJsonInSingleCall(@RequestParam("files") MultipartFile[] files, @Valid @RequestParam("customer") String customerStr) {
        this.logger.info("Number of files uploaded : {}", files.length);
        Arrays.stream(files).forEach(file -> {
            logger.info("File Name : {}", file.getOriginalFilename());
            logger.info("File Type : {}", file.getContentType());
        });
        try {
            Customer customer = mapper.readValue(customerStr, Customer.class);
            return new ResponseEntity<>(new Response(true, 0, "Files and customer details uploaded successfully", customer), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new Response(false, -1, "Customer details are not valid"), HttpStatus.BAD_REQUEST);
        }
    }
}
