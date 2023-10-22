package com.example.sportstore06.service;

import com.example.sportstore06.dao.request.UserRequest;
import com.example.sportstore06.dao.response.UserResponse;
import com.example.sportstore06.model.Product;
import com.example.sportstore06.model.Role;
import com.example.sportstore06.model.User;
import com.example.sportstore06.repository.IImageRepository;
import com.example.sportstore06.repository.IRoleRepository;
import com.example.sportstore06.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final IImageRepository iImageRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public ResponseEntity<?> findByPage(Pageable pageable) {
        Page<User> byPage = userRepository.findByPage(pageable);
        Page<UserResponse> responses = byPage.map(user -> new UserResponse(user));
        return ResponseEntity.ok().body(responses);
    }

    public ResponseEntity<?> findById(int id) {
        if (userRepository.findById(id).isPresent()) {
            return ResponseEntity.ok(userRepository.findById(id).get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id user not found");
    }

    public ResponseEntity<?> deleteById(int id) {
        try {
            if (userRepository.findById(id).isPresent()) {
                userRepository.deleteById(id);
                return ResponseEntity.accepted().build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id user not found");
            }
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    public ResponseEntity<?> changeState(Integer id, Integer state) {
        if (userRepository.findById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id user not found");
        } else if (state < 0 || state > 3) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("state not found");
        } else {
            User user = userRepository.findById(id).get();
            user.setState(state);
            userRepository.save(user);
            return ResponseEntity.accepted().build();
        }
    }

    public ResponseEntity<?> save(UserRequest request) {
        try {
            Set<Role> roles = new HashSet<>();
            for (String i : request.getRoles()) {
                Optional<Role> ObRole = roleRepository.findByName(i);
                if (ObRole.isPresent()) {
                    roles.add(ObRole.get());
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("role not found");
                }
            }
            if (iImageRepository.findById(request.getId_image()).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id image not found");
            }
            Timestamp created_at;
            Timestamp updated_at;
            if (userRepository.findById(request.getId()).isPresent()) {
                created_at = userRepository.findById(request.getId()).get().getCreated_at();
                updated_at = new Timestamp(new Date().getTime());
            } else {
                created_at = new Timestamp(new Date().getTime());
                updated_at = created_at;
            }
            var u = User.builder().
                    id(request.getId()).
                    name(request.getName()).
                    email(request.getEmail()).
                    username(request.getUsername()).
                    password(passwordEncoder.encode(request.getPassword())).
                    state(request.getState()).
                    remember_token(request.getRemember_token()).
                    created_at(created_at).
                    updated_at(updated_at).
                    roleSet(roles).
                    image(iImageRepository.findById(request.getId_image()).get()).
                    build();
            userRepository.save(u);
            return ResponseEntity.accepted().build();
        } catch (
                Exception exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

}
