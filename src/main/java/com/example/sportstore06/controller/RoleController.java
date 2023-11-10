package com.example.sportstore06.controller;

import com.example.sportstore06.dao.request.RoleRequest;
import com.example.sportstore06.dao.response.RoleResponse;
import com.example.sportstore06.model.Role;
import com.example.sportstore06.service.RoleService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/role")
@RequiredArgsConstructor
public class RoleController {
    @Value("${page_size_default}")
    private Integer page_size_default;
    private final RoleService roleService;

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Integer id) {
        try {
            if (roleService.findById(id).isPresent()) {
                RoleResponse s = new RoleResponse(roleService.findById(id).get());
                return ResponseEntity.status(HttpStatus.OK).body(s);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id role not found");
            }
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
            Page<Role> byPage = roleService.findByPage(pageable);
            Page<RoleResponse> responses = byPage.map(role -> new RoleResponse(role));
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (InvalidDataAccessApiUsageException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("filed name does not exit");
        }
    }

    @PostMapping("/save")
    private ResponseEntity<?> addRole(@Valid @RequestBody RoleRequest request) {
        try {
            if (roleService.findByName(request.getName()).isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("name already exists");
            }
            roleService.save(0, request);
            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/save/{id}")
    private ResponseEntity<?> changeRole(@Valid @RequestBody RoleRequest request,
                                         @PathVariable("id") Integer id) {
        try {
            if (roleService.findById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id role not found");
            }
            if (roleService.findByName(request.getName()).isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("name already exists");
            }
            roleService.save(id, request);
            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


}
