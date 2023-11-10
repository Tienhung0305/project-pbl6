package com.example.sportstore06.service;

import com.example.sportstore06.dao.request.BillRequest;
import com.example.sportstore06.dao.request.CategoryRequest;
import com.example.sportstore06.dao.request.ProductRequest;
import com.example.sportstore06.dao.response.CategoryResponse;
import com.example.sportstore06.dao.response.ProductResponse;
import com.example.sportstore06.model.Bill;
import com.example.sportstore06.model.Business;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final ICategoryRepository categoryRepository;
    private final IImageRepository imageRepository;

    public Optional<Category> findById(int id) {
        return categoryRepository.findById(id);
    }

    public List<Category> findByName(String name) {
        return categoryRepository.findByName(name);
    }

    public Page<Category> findByPage(Pageable pageable) {
        return categoryRepository.findByPage(pageable);
    }

    public boolean deleteById(int id) {
        try {
            categoryRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void save(int id, CategoryRequest request) {
        Timestamp created_at;
        Timestamp updated_at;
        if (categoryRepository.findById(id).isPresent()) {
            created_at = categoryRepository.findById(id).get().getCreated_at();
            updated_at = new Timestamp(new Date().getTime());
        } else {
            created_at = new Timestamp(new Date().getTime());
            updated_at = created_at;
        }

        var c = Category.builder().
                id(id).
                name(request.getName()).
                created_at(created_at).
                updated_at(updated_at).
                image(imageRepository.findById(request.getId_image()).get()).
                build();
        categoryRepository.save(c);
    }
}
