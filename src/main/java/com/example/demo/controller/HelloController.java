package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HelloController {

    Logger logger = LoggerFactory.getLogger(HelloController.class);

    @GetMapping("/hello")
    public ResponseEntity<Map<String, Object>> hello() {
        logger.info("---------------------Hello REST API--------------");
        Map<String, Object> body = Map.of(
                "message", "Hello from Spring Boot!",
                "timestamp", Instant.now().toString()
        );
        return ResponseEntity.ok(body);
    }


}
