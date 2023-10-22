package com.example.sportstore06.controller;

import com.example.sportstore06.dao.request.CommentRequest;
import com.example.sportstore06.dao.request.ProductRequest;
import com.example.sportstore06.dao.response.CommentResponse;
import com.example.sportstore06.dao.response.ProductResponse;
import com.example.sportstore06.model.Comment;
import com.example.sportstore06.model.Product;
import com.example.sportstore06.service.CommentService;
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
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/comment")
@RequiredArgsConstructor
public class CommentController {
    @Value("${page-number}")
    private Integer page_number;
    private final CommentService commentService;

    @GetMapping("/findById")
    public ResponseEntity<?> findById(@RequestParam(value = "id", required = true) Integer id) {
        return commentService.findById(id);
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
            return commentService.findByPage(pageable);
        } catch (InterpretationException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("filed not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/save")
    private ResponseEntity<?> save(@Valid @RequestBody CommentRequest commentRequest) {
        return commentService.save(commentRequest);
    }

    @DeleteMapping("/deleteById/{id}")
    private ResponseEntity<?> deleteById(@PathVariable("id") Integer id) {
        return commentService.deleteById(id);
    }
}
