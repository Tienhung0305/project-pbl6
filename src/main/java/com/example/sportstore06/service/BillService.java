package com.example.sportstore06.service;


import com.example.sportstore06.dao.request.BillRequest;
import com.example.sportstore06.model.Bill;
import com.example.sportstore06.model.BillDetail;
import com.example.sportstore06.repository.IBillDetailRepository;
import com.example.sportstore06.repository.IBillRepository;
import com.example.sportstore06.repository.IProductRepository;
import com.example.sportstore06.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillService {
    private final IBillRepository billRepository;
    private final IUserRepository userRepository;
    private final IProductRepository productRepository;
    private final IBillDetailRepository billDetailRepository;

    public Optional<Bill> findById(int id) {
        return billRepository.findById(id);
    }

    public List<Bill> findByName(String name) {
        return billRepository.findByName(name);
    }

    public Page<Bill> findByPage(Pageable pageable) {
        return billRepository.findByPage(pageable);
    }

    public boolean deleteById(int id) {
        try {
            billRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void save(int id, BillRequest request) {
        Timestamp created_at;
        Timestamp updated_at;
        if (billRepository.findById(id).isPresent()) {
            created_at = billRepository.findById(id).get().getCreated_at();
            updated_at = new Timestamp(new Date().getTime());
        } else {
            created_at = new Timestamp(new Date().getTime());
            updated_at = created_at;
        }

        var u = Bill.builder().
                id(id).
                name(request.getName()).
                information(request.getInformation()).
                total(request.getTotal()).
                user(userRepository.findById(request.getId_user()).get()).
                created_at(created_at).
                updated_at(updated_at).
                state_null(request.getState_null()).
                build();

        Set<BillDetail> set = request.getBill_detailSet().stream().map(billDetailRequest ->
                BillDetail.builder().
                        bill(u).
                        product(productRepository.findById(billDetailRequest.getId_product()).get()).
                        quantity(billDetailRequest.getQuantity()).
                        build()).collect(Collectors.toSet());
        u.setBill_detailSet(set);
        billRepository.save(u);
    }

}

