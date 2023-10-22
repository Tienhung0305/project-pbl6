package com.example.sportstore06.service;

import com.example.sportstore06.dao.request.BillDetailRequest;
import com.example.sportstore06.dao.request.BillRequest;
import com.example.sportstore06.dao.request.ProductRequest;
import com.example.sportstore06.dao.response.BillResponse;
import com.example.sportstore06.dao.response.ProductResponse;
import com.example.sportstore06.model.Bill;
import com.example.sportstore06.model.BillDetail;
import com.example.sportstore06.model.Product;
import com.example.sportstore06.repository.IBillDetailRepository;
import com.example.sportstore06.repository.IBillRepository;
import com.example.sportstore06.repository.IProductRepository;
import com.example.sportstore06.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillService {
    private final IBillRepository billRepository;
    private final IUserRepository userRepository;
    private final IProductRepository productRepository;
    private final IBillDetailRepository billDetailRepository;

    public List<Bill> findAll() {
        return billRepository.findAll();
    }

    public ResponseEntity<?> findByPage(Pageable pageable) {
        Page<Bill> byPage = billRepository.findByPage(pageable);
        Page<BillResponse> responses = byPage.map(bill -> new BillResponse(bill));
        return ResponseEntity.ok().body(responses);
    }

    public ResponseEntity<?> findById(int id) {
        if (billRepository.findById(id).isPresent()) {
            return ResponseEntity.ok(billRepository.findById(id).get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id bill not found");
    }

    public ResponseEntity<?> save(BillRequest request) {
        try {
            if (userRepository.findById(request.getId_user()).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id user not found ");
            } else {
                for (BillDetailRequest b : request.getBill_detailSet()) {
                    if (productRepository.findById(b.getId_product()).isEmpty()) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id product not found ");
                    }
                }

                Timestamp created_at;
                Timestamp updated_at;
                if (billRepository.findById(request.getId()).isPresent()) {
                    created_at = billRepository.findById(request.getId()).get().getCreated_at();
                    updated_at = new Timestamp(new Date().getTime());
                } else {
                    created_at = new Timestamp(new Date().getTime());
                    updated_at = created_at;
                }

                var u = Bill.builder().
                        id(request.getId()).
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
                return ResponseEntity.accepted().build();
            }
        } catch (
                Exception exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    public ResponseEntity<?> changeState(Integer id, Boolean state_null) {
        if (billRepository.findById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id bill not found");
        } else {
            Bill bill = billRepository.findById(id).get();
            bill.setState_null(state_null);
            billRepository.save(bill);
            return ResponseEntity.accepted().build();
        }
    }
}
