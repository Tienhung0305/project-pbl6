package com.example.sportstore06.service;

import com.example.sportstore06.dao.request.ProductInfoRequest;
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
public class ProductInfoService {
    private final IProductInfoRepository productInfoRepository;
    private final IBusinessRepository businessRepository;
    private final ISaleRepository saleRepository;
    private final ICategoryRepository categoryRepository;
    private final IImageRepository iImageRepository;

    public Long getCount() {
        return productInfoRepository.count();
    }

    public Optional<ProductInfo> findById(int id) {
        return productInfoRepository.findById(id);
    }

    public Page<ProductInfo> findByPage(Pageable pageable, Integer state) {
        return productInfoRepository.findByPage(pageable, state);
    }

    public Page<ProductInfo> findByPage(Pageable pageable) {
        return productInfoRepository.findByPage(pageable);
    }

    //begin update
    public Page<ProductInfo> findByCategory(Pageable pageable, List<Integer> categoryIds) {
        return productInfoRepository.findByCategory(pageable, categoryIds);
    }

    public Page<ProductInfo> findByCategory(Pageable pageable, Integer state, List<Integer> categoryIds) {
        return productInfoRepository.findByCategory(pageable, state, categoryIds);
    }

    public Page<ProductInfo> findByBusiness(Pageable pageable, Integer id_business) {
        return productInfoRepository.findByBusiness(pageable, id_business);
    }

    public Page<ProductInfo> findByBusiness(Pageable pageable, Integer state, Integer id_business) {
        return productInfoRepository.findByBusiness(pageable, state, id_business);
    }

    public Page<ProductInfo> findBySaleDiscount(Pageable pageable, Double discount) {
        return productInfoRepository.findBySaleDiscount(pageable, discount);
    }

    public Page<ProductInfo> findBySaleDiscount(Pageable pageable, Integer state, Double discount) {
        return productInfoRepository.findBySaleDiscount(pageable, state, discount);
    }

    public Page<ProductInfo> findBySale(Pageable pageable, Integer id_sale) {
        return productInfoRepository.findBySale(pageable, id_sale);
    }

    public Page<ProductInfo> findBySale(Pageable pageable, Integer state, Integer id_sale) {
        return productInfoRepository.findBySale(pageable, state, id_sale);
    }

    public Page<ProductInfo> SearchByName(Pageable pageable, String name) {
        return productInfoRepository.SearchByName(pageable, name);
    }

    public Page<ProductInfo> SearchByName(Pageable pageable, String name, Integer state) {
        return productInfoRepository.SearchByName(pageable, name, state);
    }

    public Page<ProductInfo> SearchByNameAndBusiness(Pageable pageable, String name, Integer id_business) {
        return productInfoRepository.SearchByNameAndBusiness(pageable, name, id_business);
    }

    public Page<ProductInfo> SearchByNameAndBusiness(Pageable pageable, String name, Integer state, Integer id_business) {
        return productInfoRepository.SearchByNameAndBusiness(pageable, name, state, id_business);
    }

    public boolean deleteById(int id) {
        try {
            productInfoRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void save(int id, ProductInfoRequest request) {
        Timestamp created_at;
        Timestamp updated_at;
        if (productInfoRepository.findById(id).isPresent()) {
            created_at = productInfoRepository.findById(id).get().getCreated_at();
            updated_at = new Timestamp(new Date().getTime());
        } else {
            created_at = new Timestamp(new Date().getTime());
            updated_at = created_at;
        }

        Set<Image> setImage = new HashSet<>();
        for (Integer i : request.getId_imageSet()) {
            setImage.add(iImageRepository.findById(i).get());
        }
        request.getId_categorySet();

        Set<Category> categories = new HashSet<>();
        for (Integer i : request.getId_categorySet()) {
            Optional<Category> Ob = categoryRepository.findById(i);
            categories.add(Ob.get());
        }

        var u = ProductInfo.builder().
                id(id).
                name(request.getName()).
                detail(request.getDetail()).
                attribute(request.getAttribute()).
                business(businessRepository.findById(request.getId_business()).get()).
                sale(request.getId_sale() == null ? null : saleRepository.findById(request.getId_sale()).get()).
                created_at(created_at).
                updated_at(updated_at).
                state(request.getState()).
                categorySet(categories).
                imageSet(setImage).
                build();
        productInfoRepository.save(u);
    }

    public void changeState(int id, int state) {
        ProductInfo productInfo = productInfoRepository.findById(id).get();
        productInfo.setState(state);
        productInfoRepository.save(productInfo);
    }
}


