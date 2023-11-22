package com.example.sportstore06.controller;

import com.example.sportstore06.dao.request.CategoryRequest;
import com.example.sportstore06.dao.response.CategoryResponse;
import com.example.sportstore06.dao.response.UserResponse;
import com.example.sportstore06.model.Category;
import com.example.sportstore06.model.User;
import com.example.sportstore06.repository.IGroupRepository;
import com.example.sportstore06.service.BusinessService;
import com.example.sportstore06.service.CategoryService;
import com.example.sportstore06.service.GroupService;
import com.example.sportstore06.service.ImageService;
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
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
@CrossOrigin
public class CategoryController {
    @Value("${page_size_default}")
    private Integer page_size_default;
    private final CategoryService categoryService;
    private final GroupService groupService;

    @GetMapping("/get-group")
    public ResponseEntity<?> getGroup() {
        return ResponseEntity.status(HttpStatus.OK).body(groupService.findAll());
    }

    @GetMapping("/get-count")
    public ResponseEntity<?> getCount() {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getCount());
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Integer id) {
        try {
            if (categoryService.findById(id).isPresent()) {
                CategoryResponse p = new CategoryResponse(categoryService.findById(id).get());
                return ResponseEntity.status(HttpStatus.OK).body(p);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id category not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam("name") String name) {
        try {
            List<Category> list = categoryService.SearchByName(name);
            List<CategoryResponse> response = list.stream().map(category -> new CategoryResponse(category)).collect(Collectors.toList());
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
            Page<Category> byPage = categoryService.findByPage(pageable);
            Page<CategoryResponse> responses = byPage.map(category -> new CategoryResponse(category));
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (InvalidDataAccessApiUsageException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("filed name does not exit");
        }
    }

    @PostMapping("/save")
    private ResponseEntity<?> addCategory(@Valid @RequestBody CategoryRequest request) {
        try {
            if (groupService.findById(request.getId_group()).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id group not found ");
            }
            categoryService.save(0, request);
            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/save/{id}")
    private ResponseEntity<?> changeCategory(@Valid @RequestBody CategoryRequest request,
                                             @PathVariable("id") Integer id) {
        try {
            if (categoryService.findById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id category not found ");
            }
            if (groupService.findById(request.getId_group()).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id group not found ");
            }
            categoryService.save(id, request);
            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    private ResponseEntity<?> deleteById(@PathVariable("id") Integer id) {
        try {
            if (categoryService.findById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id category not found");
            } else {
                boolean checkDelete = categoryService.deleteById(id);
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
