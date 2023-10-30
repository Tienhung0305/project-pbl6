package com.example.sportstore06.service;

import com.example.sportstore06.dao.request.CommentRequest;
import com.example.sportstore06.model.Product;
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
    public Optional<Sale> findById(int id) {
        return saleRepository.findById(id);
    }
    public Page<Sale> findByPage(Pageable pageable) {
        return saleRepository.findByPage(pageable);
    }
    public boolean deleteById(int id) {
        try {
            saleRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public void save(int id, Sale request) {
        var s = Sale.builder().
                id(id).
                id_business(request.getId_business()).
                started_at(request.getStarted_at()).
                ended_at(request.getEnded_at()).
                name(request.getName()).
                content(request.getContent()).
                build();
        saleRepository.save(s);
    }
}
