package com.example.sportstore06.controller;

import com.example.sportstore06.dao.request.ProductRequest;
import com.example.sportstore06.dao.request.SizeProductRequest;
import com.example.sportstore06.service.ImageService;
import com.example.sportstore06.service.ProductService;
import com.example.sportstore06.service.SizeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/size")
@RequiredArgsConstructor
@CrossOrigin()
public class SizeController {
    private final SizeService sizeService;
    private final ProductService productService;
    private final ImageService imageService;
    @PostMapping("/save")
    private ResponseEntity<?> addSize(@Valid @RequestBody SizeProductRequest request) {
        try {
            if (productService.findById(request.getId_product()).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id product not found");
            }
            sizeService.save(0, request);
            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/save/{id}")
    private ResponseEntity<?> changeSize(@Valid @RequestBody SizeProductRequest request,
                                         @PathVariable("id") Integer id) {
        try {
            if (sizeService.findById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id size not found");
            }
            if (productService.findById(request.getId_product()).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id product not found");
            }
            sizeService.save(id, request);
            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    private ResponseEntity<?> deleteById(@PathVariable("id") Integer id) {
        try {
            if (sizeService.findById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id size not found");
            } else {
                boolean checkDelete = sizeService.deleteById(id);
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
