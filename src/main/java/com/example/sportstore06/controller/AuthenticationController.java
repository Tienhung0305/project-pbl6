package com.example.sportstore06.controller;

import com.example.sportstore06.dao.request.SignInRequest;
import com.example.sportstore06.dao.request.SignUpBusinessRequest;
import com.example.sportstore06.dao.request.SignUpCustomerRequest;
import com.example.sportstore06.entity.User;
import com.example.sportstore06.security.AuthenticationService;
import com.example.sportstore06.service.EmailSenderService;
import com.example.sportstore06.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final EmailSenderService senderService;
    private final PasswordEncoder passwordEncoder;

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

    @PostMapping("/forgot-password-sender/{email}")
    public ResponseEntity<?> forgotPasswordSender(@PathVariable("email") String email) {
        if (userService.findByEmail(email).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("email not found");
        }
        User user = userService.findByEmail(email).get();
        String new_password = alphaNumericString(10);
        String old_password = user.getPassword();

        senderService.sendMail(user.getEmail(),
                "SportStore - forgot password -" + user.getUsername(),
                "\n Mật khẩu mới của bạn là :" + new_password
                        + "\n Để tiếp tục thay đổi mật khẩu vui lòng truy cập tại đây :"
                        + "\n https://project-pbl6-production.up.railway.app/api/v1/auth/forgot-password"
                        + "?old_password=" + old_password +
                        "&new_password=" + new_password +
                        "&username=" + user.getUsername());
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @GetMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(
            @RequestParam(value = "old_password", required = true) String old_password,
            @RequestParam(value = "new_password", required = true) String new_password,
            @RequestParam(value = "username", required = true) String username) {
        User user = userService.findByUsername(username).get();
        if (user.getPassword().equals(old_password)) {
            String pass = passwordEncoder.encode(new_password);
            user.setPassword(pass);
            userService.save(user);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Successful");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    public static String alphaNumericString(int len) {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }
}
