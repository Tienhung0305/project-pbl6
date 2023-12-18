package com.example.sportstore06.service;

import com.example.sportstore06.dao.request.ProductRequest;
import com.example.sportstore06.model.Product;
import com.example.sportstore06.model.ProductInfo;
import com.example.sportstore06.repository.IImageRepository;
import com.example.sportstore06.repository.IProductInfoRepository;
import com.example.sportstore06.repository.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final IProductRepository productRepository;
    private final IProductInfoRepository productInfoRepository;
    private final IImageRepository iImageRepository;

    public Optional<Product> findById(int id) {
        return productRepository.findById(id);
    }

    public List<Product> getByProductIf(Integer id_product_information) {
        return productRepository.getByProductIf(id_product_information);
    }

    public void save(int id, ProductRequest request) {
        var u = Product.builder().
                id(id).
                productInfo(productInfoRepository.findById(request.getId_product_information()).get()).
                quantity(request.getQuantity()).
                size(request.getSize()).
                price(request.getPrice()).
                build();
        productRepository.save(u);
    }

    public boolean deleteById(int id) {
        try {
            productRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
