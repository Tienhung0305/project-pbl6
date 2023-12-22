package com.example.sportstore06.service;


import com.example.sportstore06.dao.request.BillRequest;
import com.example.sportstore06.model.Bill;
import com.example.sportstore06.model.BillDetail;
import com.example.sportstore06.model.User;
import com.example.sportstore06.repository.*;
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

    public Long getCount() {
        return billRepository.count();
    }

    public Optional<Bill> findById(int id) {
        return billRepository.findById(id);
    }

    public Page<Bill> SearchByName(Pageable pageable, String name) {
        return billRepository.SearchByName(pageable, name);
    }

    public Page<Bill> findByPage(Pageable pageable) {
        return billRepository.findByPage(pageable);
    }

    public List<Bill> findByIdUser(Integer id_user) {
        return billRepository.findByIdUser(id_user);
    }


    public Page<Bill> SearchByName(Pageable pageable, String name, Integer state) {
        return billRepository.SearchByName(pageable, name, state);
    }

    public Page<Bill> findByPage(Pageable pageable, Integer state) {
        return billRepository.findByPage(pageable, state);
    }

    public boolean deleteById(int id) {
        try {
            billRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public int save(int id, BillRequest request, Integer state) {
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
                state(state).
                build();

        Set<BillDetail> set = request.getBill_detailSet()
                .stream()
                .map(billDetailRequest -> BillDetail.builder().
                        bill(u).
                        product(productRepository.findById(billDetailRequest.getId_product()).get()).
                        quantity(billDetailRequest.getQuantity()).
                        price(billDetailRequest.getPrice()).
                        build()).collect(Collectors.toSet());
        u.setBill_detailSet(set);
        billRepository.save(u);
        return u.getId();
    }

    public void changeState(int id, int state) {
        Bill bill = billRepository.findById(id).get();
        bill.setState(state);
        billRepository.save(bill);
    }

}

