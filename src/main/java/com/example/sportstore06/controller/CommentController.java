package com.example.sportstore06.controller;

import com.example.sportstore06.dao.request.CommentRequest;
import com.example.sportstore06.dao.response.CommentResponse;
import com.example.sportstore06.model.Comment;
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
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
@CrossOrigin
public class CommentController {
    @Value("${page_size_default}")
    private Integer page_size_default;
    private final CommentService commentService;
    private final ProductInfoService productInfoService;
    private final UserService userService;
    private final ImageService imageService;

    @GetMapping("/get-count")
    public ResponseEntity<?> getCount() {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getCount());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Integer id) {
        try {
            if (commentService.findById(id).isPresent()) {
                CommentResponse p = new CommentResponse(
                        commentService.findById(id).get(),
                        commentService.findByReply(id));
                return ResponseEntity.status(HttpStatus.OK).body(p);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id comment not found");
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

            Page<Comment> byPage = commentService.findByPage(pageable);
            Page<CommentResponse> responses = byPage.map(comment ->
                    new CommentResponse(comment, commentService.findByReply(comment.getId())));
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (InvalidDataAccessApiUsageException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("filed name does not exit");
        }
    }

    @GetMapping("/find-by-product/{id_product_info}")
    public ResponseEntity<?> findByProduct(@PathVariable("id_product_info") Integer id_product_info,
                                           @RequestParam(value = "page", required = false) Optional<Integer> page,
                                           @RequestParam(value = "page_size", required = false) Optional<Integer> page_size,
                                           @RequestParam(value = "sort", required = false) String sort,
                                           @RequestParam(value = "desc", required = false) Optional<Boolean> desc) {
        try {
            if (productInfoService.findById(id_product_info).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id product information not found");
            }
            Pageable pageable;
            if (sort != null) {
                pageable = PageRequest.of(page.orElse(0), page_size.orElse(page_size_default),
                        desc.orElse(true) ? Sort.by(sort).descending() : Sort.by(sort).ascending());
            } else {
                pageable = PageRequest.of(page.orElse(0), page_size.orElse(page_size_default));
            }
            Page<Comment> byPage = commentService.findByProductInfo(pageable, id_product_info);
            Page<CommentResponse> responses = byPage.map(comment ->
                    new CommentResponse(comment, commentService.findByReply(comment.getId())));
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (InvalidDataAccessApiUsageException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("filed name does not exit");
        }
    }

    @PostMapping("/save")
    private ResponseEntity<?> addComment(@Valid @RequestBody CommentRequest request) {
        try {
            for (int id_image : request.getId_imageSet()) {
                if (imageService.findById(id_image).isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id image not found");
                }
            }
            if (productInfoService.findById(request.getId_product_information()).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id product information not found ");
            } else if (userService.findById(request.getId_user()).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id user not found");
            } else if (request.getReply() != null && commentService.findById(request.getReply()).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id reply not found");
            } else {
                commentService.save(0, request);
                return ResponseEntity.accepted().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/save/{id}")
    private ResponseEntity<?> changeComment(@Valid @RequestBody CommentRequest request,
                                            @PathVariable("id") Integer id) {
        try {
            if (commentService.findById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id comment not found");
            }
            for (int id_image : request.getId_imageSet()) {
                if (imageService.findById(id_image).isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id image not found");
                }
            }
            if (productInfoService.findById(request.getId_product_information()).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id product information not found ");
            }
            if (userService.findById(request.getId_user()).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id user not found");
            }
            if (request.getReply() != null && commentService.findById(request.getReply()).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id reply not found");
            }
            commentService.save(id, request);
            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    private ResponseEntity<?> deleteById(@PathVariable("id") Integer id) {
        try {
            if (commentService.findById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id comment not found");
            } else {
                boolean checkDelete = commentService.deleteById(id);
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
