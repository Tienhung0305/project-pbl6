package com.example.sportstore06.controller;


import com.example.sportstore06.dao.request.BusinessRequest;
import com.example.sportstore06.dao.response.BusinessResponse;
import com.example.sportstore06.entity.Business;
import com.example.sportstore06.service.*;
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

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/business")
@RequiredArgsConstructor
public class BusinessController {
    @Value("${page_size_default}")
    private Integer page_size_default;
    private final BusinessService businessService;
    private final UserService userService;
    private final ImageService imageService;
    private final CategoryService categoryService;

    @GetMapping("/get-count")
    public ResponseEntity<?> getCount() {
        return ResponseEntity.status(HttpStatus.OK).body(businessService.getCount());
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Integer id) {
        try {
            if (businessService.findById(id).isPresent()) {
                BusinessResponse b = new BusinessResponse(businessService.findById(id).get());
                return ResponseEntity.status(HttpStatus.OK).body(b);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id business not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam(value = "name", required = true) String name,
                                    @RequestParam(value = "page", required = false) Optional<Integer> page,
                                    @RequestParam(value = "page_size", required = false) Optional<Integer> page_size,
                                    @RequestParam(value = "sort", required = false) String sort,
                                    @RequestParam(value = "desc", required = false) Optional<Boolean> desc,
                                    @RequestParam(value = "state", required = false) Optional<Integer> state) {
        try {
            Pageable pageable;
            if (sort != null) {
                pageable = PageRequest.of(page.orElse(0), page_size.orElse(page_size_default),
                        desc.orElse(true) ? Sort.by(sort).descending() : Sort.by(sort).ascending());
            } else {
                pageable = PageRequest.of(page.orElse(0), page_size.orElse(page_size_default));
            }
            Page<Business> byPage;
            if (state.isPresent() && state.get() >= 0 && state.get() <= 3) {
                byPage = businessService.SearchByName(pageable, name, state.get());
            } else {
                byPage = businessService.SearchByName(pageable, name);
            }
            Page<BusinessResponse> responses = byPage.map(business -> business != null ? new BusinessResponse(business) : null);
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (InvalidDataAccessApiUsageException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("filed name does not exit");
        }
    }

    @GetMapping()
    public ResponseEntity<?> findByPage(@RequestParam(value = "page", required = false) Optional<Integer> page,
                                        @RequestParam(value = "page_size", required = false) Optional<Integer> page_size,
                                        @RequestParam(value = "sort", required = false) String sort,
                                        @RequestParam(value = "desc", required = false) Optional<Boolean> desc,
                                        @RequestParam(value = "state", required = false) Optional<Integer> state) {
        try {
            Pageable pageable;
            if (sort != null) {
                pageable = PageRequest.of(page.orElse(0), page_size.orElse(page_size_default),
                        desc.orElse(true) ? Sort.by(sort).descending() : Sort.by(sort).ascending());
            } else {
                pageable = PageRequest.of(page.orElse(0), page_size.orElse(page_size_default));
            }
            Page<Business> byPage;
            if (state.isPresent() && state.get() >= 0 && state.get() <= 3) {
                byPage = businessService.findByPage(pageable, state.get());
            } else {
                byPage = businessService.findByPage(pageable);
            }
            Page<BusinessResponse> responses = byPage.map(business -> business != null ? new BusinessResponse(business) : null);
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (InvalidDataAccessApiUsageException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("filed name does not exit");
        }
    }

    @PostMapping("/save")
    private ResponseEntity<?> addBusiness(@Valid @RequestBody BusinessRequest request) {
        try {
            if (userService.findById(request.getId_user()).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id user not found ");
            }
            //check exits
            if (businessService.findByName(request.getName()).isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("name already exists");
            }
            businessService.save(0, request);
            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/save/{id}")
    private ResponseEntity<?> changeBusiness(@Valid @RequestBody BusinessRequest request,
                                             @PathVariable Integer id) {
        try {
            if (businessService.findById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id business not found ");
            }
            if (userService.findById(request.getId_user()).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id user not found ");
            }
            //check exits
            if (businessService.findByName(request.getName()).isPresent() &&
                    businessService.findByName(request.getName()).get().getId() != id) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("name already exists");
            }
            businessService.save(request.getId_user(), request);
            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}