package com.example.sportstore06.controller;


import com.example.sportstore06.dao.request.UserRequest;

import com.example.sportstore06.dao.response.UserResponse;
import com.example.sportstore06.model.Role;
import com.example.sportstore06.model.User;
import com.example.sportstore06.service.ImageService;
import com.example.sportstore06.service.RoleService;
import com.example.sportstore06.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    @Value("${page_size_default}")
    private Integer page_size_default;
    private final UserService userService;
    private final RoleService roleService;
    private final ImageService imageService;

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Integer id, @AuthenticationPrincipal User user) {
        try {
            if (userService.findById(id).isPresent()) {
                UserResponse u = new UserResponse(userService.findById(id).get());
                return ResponseEntity.status(HttpStatus.OK).body(u);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id user not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam("name") String name) {
        try {
            List<User> list = userService.findByName(name);
            List<UserResponse> response = list.stream().map(user -> new UserResponse(user)).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping()
    public ResponseEntity<?> findByPage(@RequestParam(value = "page", required = false) Optional<Integer> page,
                                        @RequestParam(value = "page_size", required = false) Optional<Integer> page_size,
                                        @RequestParam(value = "sort", required = false) String sort,
                                        @RequestParam(value = "desc", required = false) Optional<Boolean> desc) {
        try {
            Pageable pageable;
            if (sort != null) {
                pageable = PageRequest.of(page.orElse(0), page_size.orElse(page_size_default),
                        desc.orElse(true) ? Sort.by(sort).descending() : Sort.by(sort).ascending());
            } else {
                pageable = PageRequest.of(page.orElse(0), page_size.orElse(page_size_default));
            }
            Page<User> byPage = userService.findByPage(pageable);
            Page<UserResponse> responses = byPage.map(user -> new UserResponse(user));
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (InvalidDataAccessApiUsageException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("filed name does not exit");
        }
    }

    @PostMapping("/save")
    private ResponseEntity<?> addUser(@Valid @RequestBody UserRequest request) {
        try {
            for (String i : request.getRoles()) {
                Optional<Role> ObRole = roleService.findByName(i);
                if (ObRole.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("role not found");
                }
            }
            if (imageService.findById(request.getId_image()).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id image not found");
            }

            //check exits
            if (userService.findByUsername(request.getUsername()).isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("username already exists");
            }
            if (userService.findByEmail(request.getEmail()).isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("email already exists");
            }
            if (userService.findByCic(request.getCic()).isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("citizen identification card already exists");
            }
            if (userService.findByPhone(request.getPhone()).isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("phone number already exists");
            }
            //

            userService.save(0, request);
            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/save/{id}")
    private ResponseEntity<?> changeUser(@Valid @RequestBody UserRequest request,
                                         @PathVariable("id") Integer id) {
        try {
            if (userService.findById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id user not found");
            }
            for (String i : request.getRoles()) {
                Optional<Role> ObRole = roleService.findByName(i);
                if (ObRole.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("role not found");
                }
            }
            if (imageService.findById(request.getId_image()).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id image not found");
            }

            //check exits
            if (userService.findByUsername(request.getUsername()).isPresent() &&
                    userService.findByUsername(request.getUsername()).get().getId() != id) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("username already exists");
            }
            if (userService.findByEmail(request.getEmail()).isPresent() &&
                    userService.findByEmail(request.getEmail()).get().getId() != id) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("email already exists");
            }
            if (userService.findByCic(request.getCic()).isPresent() &&
                    userService.findByCic(request.getCic()).get().getId() != id) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("citizen identification card already exists");
            }
            if (userService.findByPhone(request.getPhone()).isPresent() &&
                    userService.findByPhone(request.getPhone()).get().getId() != id) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("phone number already exists");
            }
            //

            userService.save(id, request);
            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/change-state/{id}")
    private ResponseEntity<?> changeState(@PathVariable("id") Integer id,
                                          @RequestParam(value = "state", required = true) Integer state) {
        try {
            if (userService.findById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id user not found");
            } else if (state < 0 || state > 3) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("state not found");
            } else {
                userService.changeState(id, state);
                return ResponseEntity.accepted().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    private ResponseEntity<?> deleteById(@PathVariable("id") Integer id) {
        try {
            if (userService.findById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id user not found");
            } else {
                boolean checkDelete = userService.deleteById(id);
                if (!checkDelete) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("can't delete");
                }
                return ResponseEntity.accepted().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


}
