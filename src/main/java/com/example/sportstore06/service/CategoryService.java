package com.example.sportstore06.service;

import com.example.sportstore06.dao.request.BillRequest;
import com.example.sportstore06.dao.request.CategoryRequest;
import com.example.sportstore06.dao.request.ProductRequest;
import com.example.sportstore06.dao.response.CategoryResponse;
import com.example.sportstore06.dao.response.ProductResponse;
import com.example.sportstore06.model.Bill;
import com.example.sportstore06.model.Category;
import com.example.sportstore06.model.Product;
import com.example.sportstore06.repository.ICategoryRepository;
import com.example.sportstore06.repository.IImageRepository;
import com.example.sportstore06.repository.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final ICategoryRepository categoryRepository;
    private final IImageRepository imageRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public ResponseEntity<?> findByPage(Pageable pageable) {
        Page<Category> byPage = categoryRepository.findByPage(pageable);
        Page<CategoryResponse> responses = byPage.map(category -> new CategoryResponse(category));
        return ResponseEntity.ok().body(responses);
    }

    public ResponseEntity<?> findById(int id) {
        if (categoryRepository.findById(id).isPresent()) {
            return ResponseEntity.ok(categoryRepository.findById(id).get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id bill not found");
    }

    public ResponseEntity<?> save(CategoryRequest request) {
        if (imageRepository.findById(request.getId_image()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id image not found ");
        } else {
            Timestamp created_at;
            Timestamp updated_at;
            if (categoryRepository.findById(request.getId()).isPresent()) {
                created_at = categoryRepository.findById(request.getId()).get().getCreated_at();
                updated_at = new Timestamp(new Date().getTime());
            } else {
                created_at = new Timestamp(new Date().getTime());
                updated_at = created_at;
            }

            var c = Category.builder().
                    id(request.getId()).
                    name(request.getName()).
                    created_at(created_at).
                    updated_at(updated_at).
                    image(imageRepository.findById(request.getId_image()).get()).
                    build();
            categoryRepository.save(c);
            return ResponseEntity.accepted().build();
        }
    }

    public ResponseEntity<?> deleteById(int id) {
        try {
            if (categoryRepository.findById(id).isPresent()) {
                categoryRepository.deleteById(id);
                return ResponseEntity.accepted().build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id category not found");
            }
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }
}
