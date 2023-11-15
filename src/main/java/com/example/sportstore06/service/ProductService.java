package com.example.sportstore06.service;

import com.example.sportstore06.dao.request.ProductRequest;
import com.example.sportstore06.model.*;
import com.example.sportstore06.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final IProductRepository productRepository;
    private final IBusinessRepository businessRepository;
    private final ISaleRepository saleRepository;
    private final ICategoryRepository categoryRepository;
    private final IImageRepository iImageRepository;

    public Optional<Product> findById(int id) {
        return productRepository.findById(id);
    }

    public List<Product> SearchByName(String name) {
        return productRepository.SearchByName(name);
    }

    public Page<Product> findByPage(Pageable pageable) {
        return productRepository.findByPage(pageable);
    }

    //begin update
    public List<Product> findByBrand(String brand)
    {
        return productRepository.findByBrand(brand);
    }
    public Page<Product> findByCategory(Pageable pageable, Integer id_category) {
        return productRepository.findByCategory(pageable, id_category);
    }
    public Page<Product> findByBusiness(Pageable pageable, Integer id_business) {
        return productRepository.findByBusiness(pageable, id_business);
    }
    public Page<Product> findBySale(Pageable pageable, Integer id_sale) {
        return productRepository.findBySale(pageable, id_sale);
    }
    public Page<Product> findByBrand(Pageable pageable, String brand) {
        return productRepository.findByBrand(pageable, brand);
    }
    //
    public Page<Product> findByCategory(Pageable pageable, Integer state, Integer id_category) {
        return productRepository.findByCategory(pageable, state, id_category);
    }
    public Page<Product> findByBusiness(Pageable pageable,Integer state, Integer id_business) {
        return productRepository.findByBusiness(pageable, state, id_business);
    }
    public Page<Product> findBySale(Pageable pageable,Integer state, Integer id_sale) {
        return productRepository.findBySale(pageable,state, id_sale);
    }
    public Page<Product> findByBrand(Pageable pageable,Integer state, String brand) {
        return productRepository.findByBrand(pageable,state, brand);
    }

    //end

    public List<Product> SearchByName(String name, Integer state) {
        return productRepository.SearchByName(name, state);
    }

    public Page<Product> findByPage(Pageable pageable, Integer state) {
        return productRepository.findByPage(pageable, state);
    }

    public boolean deleteById(int id) {
        try {
            productRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void save(int id, ProductRequest request) {
        Timestamp created_at;
        Timestamp updated_at;
        if (productRepository.findById(id).isPresent()) {
            created_at = productRepository.findById(id).get().getCreated_at();
            updated_at = new Timestamp(new Date().getTime());
        } else {
            created_at = new Timestamp(new Date().getTime());
            updated_at = created_at;
        }

        Set<Image> setImage = new HashSet<>();
        for (int i : request.getId_imageSet()) {
            setImage.add(iImageRepository.findById(i).get());
        }
        var u = Product.builder().
                id(id).
                name(request.getName()).
                detail(request.getDetail()).
                price(request.getPrice()).
                attribute(request.getAttribute()).
                brand(request.getBrand()).
                quantity(request.getQuantity()).
                business(businessRepository.findById(request.getId_business()).get()).
                sale(request.getId_category() == null ? null : saleRepository.findById(request.getId_sale()).get()).
                category(request.getId_category() == null ? null : categoryRepository.findById(request.getId_category()).get()).
                created_at(created_at).
                updated_at(updated_at).
                state(request.getState()).
                imageSet(setImage).
                build();
        productRepository.save(u);
    }

    public void changeState(int id, int state) {
        Product product = productRepository.findById(id).get();
        product.setState(state);
        productRepository.save(product);
    }
}


