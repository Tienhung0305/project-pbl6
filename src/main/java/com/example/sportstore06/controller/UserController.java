package com.example.sportstore06.controller;


import com.example.sportstore06.dao.request.UserRequest;
import com.example.sportstore06.dao.response.UserResponse;
import com.example.sportstore06.model.User;
import com.example.sportstore06.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.sqm.InterpretationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    @Value("${page-number}")
    private Integer page_number;

    private final UserService userService;

    @GetMapping("/findById")
    public ResponseEntity<?> findById(@RequestParam(value = "id", required = true) Integer id) {
        return userService.findById(id);
    }

    @GetMapping("/findByPage")
    public ResponseEntity<?> findByPage(@RequestParam(value = "page", required = false) Optional<Integer> page,
                                        @RequestParam(value = "page_size", required = false) Optional<Integer> page_size,
                                        @RequestParam(value = "sort", required = false) String sort,
                                        @RequestParam(value = "desc", required = false) Optional<Boolean> desc) {
        try {
            Pageable pageable;
            if (sort != null) {
                pageable = PageRequest.of(page.orElse(0), page_size.orElse(page_number),
                        desc.orElse(true) ? Sort.by(sort).descending() : Sort.by(sort).ascending());
            } else {
                pageable = PageRequest.of(page.orElse(0), page_size.orElse(page_number));
            }
            return userService.findByPage(pageable);
        } catch (InterpretationException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("filed not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(userService.findAll().stream().map(user -> new UserResponse(user)));
    }

    @PostMapping("/save")
    private ResponseEntity<?> save(@Valid @RequestBody UserRequest userRequest) {
        return userService.save(userRequest);
    }

    @PostMapping("/changeState")
    private ResponseEntity<?> changeState(@RequestParam(value = "id", required = true) Integer id,
                                          @RequestParam(value = "state", required = true) Integer state) {
        return userService.changeState(id, state);
    }

    @DeleteMapping("/deleteById/{id}")
    private ResponseEntity<?> deleteById(@PathVariable("id") Integer id) {
        return userService.deleteById(id);
    }

}
