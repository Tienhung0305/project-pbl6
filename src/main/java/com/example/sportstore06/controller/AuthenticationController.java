package com.example.sportstore06.controller;

import com.example.sportstore06.dao.response.JwtAuthenticationResponse;
import com.example.sportstore06.dao.request.SignUpRequest;
import com.example.sportstore06.dao.request.SignInRequest;
import com.example.sportstore06.model.Role;
import com.example.sportstore06.security.AuthenticationService;
import com.example.sportstore06.service.RoleService;
import com.example.sportstore06.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final RoleService roleService;

    @PostMapping("/signup")
    public ResponseEntity<?> SignUp(@Valid @RequestBody SignUpRequest request) {
        Boolean checkRole = true;
        for (String i : request.getRoles()) {
            Optional<Role> ObRole = roleService.findByName(i);
            if (ObRole.isEmpty()) {
                checkRole = false;
            }
        }
        if (userService.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("username already exists");
        } else if (userService.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("email already exists");
        } else if (userService.findByCic(request.getCic()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("citizen identification card already exists");
        } else if (userService.findByPhone(request.getPhone()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("phone number already exists");
        } else if (!checkRole) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("role not found");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(authenticationService.signup(request));
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> SignIn(@Valid @RequestBody SignInRequest request) {
        return ResponseEntity.ok(authenticationService.signin(request));
    }
}
