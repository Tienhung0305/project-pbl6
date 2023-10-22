package com.example.sportstore06.controller;

import com.example.sportstore06.dao.response.JwtAuthenticationResponse;
import com.example.sportstore06.dao.request.SignUpRequest;
import com.example.sportstore06.dao.request.SignInRequest;
import com.example.sportstore06.security.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignUpRequest request) {
        if (authenticationService.signup(request) != null) {
            return ResponseEntity.ok(authenticationService.signup(request));
        } else {
            return ResponseEntity.badRequest().body("Role not found");
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@Valid @RequestBody SignInRequest request) {
        return ResponseEntity.ok(authenticationService.signin(request));
    }
}
