package com.example.sportstore06.service;

import com.example.sportstore06.dao.request.ProductRequest;
import com.example.sportstore06.dao.request.UserRequest;
import com.example.sportstore06.dao.response.ProductResponse;
import com.example.sportstore06.dao.response.UserResponse;
import com.example.sportstore06.model.Product;
import com.example.sportstore06.model.Role;
import com.example.sportstore06.model.User;
import com.example.sportstore06.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final IProductRepository productRepository;
    private final IBusinessRepository businessRepository;
    private final ISaleRepository saleRepository;
    private final ICategoryRepository categoryRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public ResponseEntity<?> findByPage(Pageable pageable) {
        Page<Product> byPage = productRepository.findByPage(pageable);
        Page<ProductResponse> responses = byPage.map(product -> new ProductResponse(product));
        return ResponseEntity.ok().body(responses);
    }

    public ResponseEntity<?> findById(int id) {
        if (productRepository.findById(id).isPresent()) {
            return ResponseEntity.ok(productRepository.findById(id).map(product -> new ProductResponse(product)).get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id product not found");
    }

    public ResponseEntity<?> save(ProductRequest request) {
        try {
            if (businessRepository.findById(request.getId_business()).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id business not found ");
            } else if (request.getId_sale() != null && saleRepository.findById(request.getId_sale()).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id sale not found");
            } else if (request.getId_sale() != null && categoryRepository.findById(request.getId_category()).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id category not found");
            } else {
                var u = Product.builder().
                        id(request.getId()).
                        name(request.getName()).
                        price(request.getPrice()).
                        business(businessRepository.findById(request.getId_business()).orElse(null)).
                        sale(saleRepository.findById(request.getId_sale()).orElse(null)).
                        category(categoryRepository.findById(request.getId_category()).orElse(null)).
                        created_at(request.getCreated_at()).
                        updated_at(request.getUpdated_at()).
                        state(request.getState()).
                        imageSet(request.getImageSet()).
                        build();
                productRepository.save(u);
                return ResponseEntity.accepted().build();
            }
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    public ResponseEntity<?> changeState(Integer id, Integer state) {
        if (productRepository.findById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id product not found");
        } else if (state < 0 || state > 3) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("state not found");
        } else {
            Product product = productRepository.findById(id).get();
            product.setState(state);
            productRepository.save(product);
            return ResponseEntity.accepted().build();
        }
    }

    public ResponseEntity<?> deleteById(int id) {
        try {
            if (productRepository.findById(id).isPresent()) {
                productRepository.deleteById(id);
                return ResponseEntity.accepted().build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id product not found");
            }
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }
}
