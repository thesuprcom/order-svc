package com.supr.orderservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    @GetMapping("/healthcheck/")
    public ResponseEntity checkHealthCheck(){
        return ResponseEntity.ok("Application is up!!");
    }
}
