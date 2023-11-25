package com.example.sportstore06.controller;

import com.example.sportstore06.dao.request.ProductRequest;
import com.example.sportstore06.dao.response.ProductInfoResponse;
import com.example.sportstore06.dao.response.ProductResponse;
import com.example.sportstore06.service.ImageService;
import com.example.sportstore06.service.ProductInfoService;
import com.example.sportstore06.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
@CrossOrigin()
public class ProductController {
    private final ProductService productService;
    private final ProductInfoService productInfoService;
    private final ImageService imageService;

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Integer id) {
        try {
            if (productService.findById(id).isPresent()) {
                ProductResponse p = new ProductResponse(productService.findById(id).get());
                return ResponseEntity.status(HttpStatus.OK).body(p);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id product not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/get-quantity/{id}")
    public ResponseEntity<?> getQuantity(@PathVariable("id") Integer id) {
        try {
            if (productService.findById(id).isPresent()) {
                return ResponseEntity.status(HttpStatus.OK).body(productService.findById(id).get().getQuantity());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id product not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/save")
    private ResponseEntity<?> addProduct(@Valid @RequestBody ProductRequest request) {
        try {
            if (productInfoService.findById(request.getId_product_information()).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id product information not found");
            }
            productService.save(0, request);
            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/save/{id}")
    private ResponseEntity<?> changeProduct(@Valid @RequestBody ProductRequest request,
                                         @PathVariable("id") Integer id) {
        try {
            if (productService.findById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id product not found");
            }
            if (productInfoService.findById(request.getId_product_information()).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id product information not found");
            }
            productService.save(id, request);
            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    private ResponseEntity<?> deleteById(@PathVariable("id") Integer id) {
        try {
            if (productService.findById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id product not found");
            } else {
                boolean checkDelete = productService.deleteById(id);
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
