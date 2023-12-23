package com.example.sportstore06.controller;

import com.example.sportstore06.dao.request.SignUpBusinessRequest;
import com.example.sportstore06.dao.request.SignUpCustomerRequest;
import com.example.sportstore06.dao.request.SignInRequest;
import com.example.sportstore06.security.AuthenticationService;
import com.example.sportstore06.service.RoleService;
import com.example.sportstore06.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final RoleService roleService;

    @PostMapping("/signup-customer")
    public ResponseEntity<?> signUpCustomer(@Valid @RequestBody SignUpCustomerRequest request) {
        if (userService.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("username already exists");
        } else if (userService.findByPhone(request.getPhone()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("phone number already exists");
        } else if (userService.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("email already exists");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(authenticationService.signupCustomer(request));
        }
    }

    @PostMapping("/signup-business")
    public ResponseEntity<?> signUpBusiness(@Valid @RequestBody SignUpBusinessRequest request) {
        if (userService.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("username already exists");
        } else if (userService.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("email already exists");
        } else if (userService.findByCic(request.getCic()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("citizen identification card already exists");
        } else if (userService.findByPhone(request.getPhone()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("phone number already exists");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(authenticationService.signUpBusiness(request));
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInRequest request) {
        if (userService.findByUsername(request.getUsername()).isPresent()) {
            if (userService.findByUsername(request.getUsername()).get().getState() != 0) {
                return ResponseEntity.status(HttpStatus.GONE).body("your account does not exist or has been deleted.");
            }
            return ResponseEntity.ok(authenticationService.signin(request));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("your account does not exist");
    }
}
