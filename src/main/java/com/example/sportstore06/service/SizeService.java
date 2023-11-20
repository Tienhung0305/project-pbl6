package com.example.sportstore06.service;

import com.example.sportstore06.dao.request.ProductRequest;
import com.example.sportstore06.dao.request.SizeProductRequest;
import com.example.sportstore06.model.Category;
import com.example.sportstore06.model.Image;
import com.example.sportstore06.model.Product;
import com.example.sportstore06.model.SizeProduct;
import com.example.sportstore06.repository.IProductRepository;
import com.example.sportstore06.repository.ISizeProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SizeService {
    private final ISizeProductRepository sizeProductRepository;
    private final IProductRepository productRepository;
    public Optional<SizeProduct> findById(int id) {
        return sizeProductRepository.findById(id);
    }

    public void save(int id, SizeProductRequest request) {
        var u = SizeProduct.builder().
                id(id).
                product(productRepository.findById(request.getId_product()).get()).
                size(request.getSize()).
                color(request.getColor()).
                build();
        sizeProductRepository.save(u);
    }

    public boolean deleteById(int id) {
        try {
            sizeProductRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
