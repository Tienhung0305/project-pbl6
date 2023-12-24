package com.example.sportstore06.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class AuthorizationController {
    @GetMapping
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hi Visiter");
    }

    @GetMapping("/admin")
    public ResponseEntity<String> sayHelloAdmin() {
        return ResponseEntity.ok("Hi Admin");
    }

    @GetMapping("/business")
    public ResponseEntity<String> sayHelloBusiness() {
        return ResponseEntity.ok("Hi Business");
    }

    @GetMapping("/customer")
    public ResponseEntity<String> sayHelloCustomer() {
        return ResponseEntity.ok("Hi Customer");
    }
}