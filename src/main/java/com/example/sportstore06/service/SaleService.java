package com.example.sportstore06.service;

import com.example.sportstore06.dao.request.SaleRequest;
import com.example.sportstore06.model.Sale;
import com.example.sportstore06.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SaleService {
    private final ISaleRepository saleRepository;
    private final IImageRepository imageRepository;
    private final IBusinessRepository businessRepository;

    public Long getCount() {
        return saleRepository.count();
    }

    public Double getMaxDiscount() {
        return saleRepository.getMaxDiscount();
    }
    public Double getMinDiscount() {
        return saleRepository.getMinDiscount();
    }

    public Optional<Sale> findById(int id) {
        return saleRepository.findById(id);
    }

    public Page<Sale> SearchByName(Pageable pageable, String name) {
        return saleRepository.SearchByName(pageable, name);
    }

    public Page<Sale> findByPage(Pageable pageable) {
        return saleRepository.findByPage(pageable);
    }

    public Page<Sale> findByIdBusiness(Pageable pageable, Integer id_business) {
        return saleRepository.findByIdBusiness(pageable, id_business);
    }

    public Page<Sale> findByDiscount(Pageable pageable, Double discount_min, Double discount_max) {
        return saleRepository.findByDiscount(pageable, discount_min, discount_max);
    }

    public Page<Sale> findByDiscount(Pageable pageable, Double discount_min, Double discount_max, Integer id_business) {
        return saleRepository.findByDiscount(pageable, discount_min, discount_max, id_business);
    }

    public boolean deleteById(int id) {
        try {
            saleRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void save(int id, SaleRequest request) {
        var s = Sale.builder().
                id(id).
                discount(request.getDiscount()).
                business(businessRepository.findById(request.getId_business()).get()).
                started_at(request.getStarted_at()).
                ended_at(request.getEnded_at()).
                name(request.getName()).
                content(request.getContent()).
                url(request.getUrl()).
                build();
        saleRepository.save(s);
    }
}
