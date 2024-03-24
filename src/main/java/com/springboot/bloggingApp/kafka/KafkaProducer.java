package com.springboot.bloggingApp.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

@Component
public class KafkaProducer {
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    private static final String Topic = "NewTopic";

    public String showUser(@RequestBody String userName) {
        this.kafkaTemplate.send(Topic, userName);
        return "Check your Console";
    }
}
