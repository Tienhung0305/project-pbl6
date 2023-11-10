package com.example.sportstore06.controller;

import com.example.sportstore06.dao.request.CommentRequest;
import com.example.sportstore06.dao.request.ImageRequest;
import com.example.sportstore06.dao.response.CommentResponse;
import com.example.sportstore06.dao.response.ImageResponse;
import com.example.sportstore06.dao.response.UserResponse;
import com.example.sportstore06.model.Comment;
import com.example.sportstore06.model.Image;
import com.example.sportstore06.model.User;
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
@RequestMapping("/api/v1/image")
@RequiredArgsConstructor
public class ImageController {
    @Value("${page_size_default}")
    private Integer page_size_default;
    private final ImageService imageService;

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Integer id) {
        try {
            if (imageService.findById(id).isPresent()) {
                ImageResponse i = new ImageResponse(imageService.findById(id).get());
                return ResponseEntity.status(HttpStatus.OK).body(i);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id image not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam("name") String name) {
        try {
            List<Image> list = imageService.findByName(name);
            List<ImageResponse> response = list.stream().map(image -> new ImageResponse(image)).collect(Collectors.toList());
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
            Page<Image> byPage = imageService.findByPage(pageable);
            Page<ImageResponse> responses = byPage.map(image -> new ImageResponse(image));
            return ResponseEntity.status(HttpStatus.OK).body(responses);
        } catch (InvalidDataAccessApiUsageException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("filed name does not exit");
        }
    }

    @PostMapping("/save")
    private ResponseEntity<?> addImage(@Valid @RequestBody ImageRequest request) {
        try {
            imageService.save(0, request);
            return ResponseEntity.accepted().build();
        } catch (
                Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/save/{id}")
    private ResponseEntity<?> changeImage(@Valid @RequestBody ImageRequest request,
                                          @PathVariable("id") Integer id) {
        try {
            if (imageService.findById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id image not found");
            }
            imageService.save(id, request);
            return ResponseEntity.accepted().build();
        } catch (
                Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/change-url/{id}")
    private ResponseEntity<?> changeImageUrl(@Valid @RequestBody ImageRequest request,
                                             @PathVariable("id") Integer id,
                                             @RequestParam("url") String url) {
        try {
            if (imageService.findById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id image not found");
            }
            imageService.change_url(id, url);
            return ResponseEntity.accepted().build();
        } catch (
                Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    private ResponseEntity<?> deleteById(@PathVariable("id") Integer id) {
        try {
            if (imageService.findById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id image not found");
            } else {
                boolean checkDelete = imageService.deleteById(id);
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
