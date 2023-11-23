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
import com.example.sportstore06.repository.IGroupRepository;
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
    private final IGroupRepository groupRepository;

    public Long getCount() {
        return categoryRepository.count();
    }
    public Optional<Category> findById(int id) {
        return categoryRepository.findById(id);
    }

    public Page<Category> SearchByName(Pageable pageable, String name) {
        return categoryRepository.SearchByName(pageable, name);
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

        var c = Category.builder().
                id(id).
                name(request.getName()).
                group(groupRepository.findById(request.getId_group()).get()).
                build();
        categoryRepository.save(c);
    }
}
