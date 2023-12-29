package com.example.sportstore06.service;


import com.example.sportstore06.dao.request.BillRequest;
import com.example.sportstore06.entity.Bill;
import com.example.sportstore06.entity.BillDetail;
import com.example.sportstore06.entity.ProductInfo;
import com.example.sportstore06.entity.Transaction;
import com.example.sportstore06.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillService {
    private final IBillRepository billRepository;
    private final IUserRepository userRepository;
    private final IProductRepository productRepository;
    private final IBusinessRepository businessRepository;
    private final ITransactionRepository transactionRepository;
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

    public Page<Bill> findByIdBusiness(Pageable pageable, Integer id_business, Integer state) {
        if (state != null) {
            return billRepository.findByIdBusiness(pageable, id_business, state);
        }
        return billRepository.findByIdBusiness(pageable, id_business);
    }

    public List<Bill> findByIdUser(Integer id_user, Integer state) {
        if (state != null) {
            return billRepository.findByIdUser(id_user, state);
        }
        return billRepository.findByIdUser(id_user);
    }

    public List<Bill> findByTransactionId(Integer transaction_id) {
        return billRepository.findByTransactionId(transaction_id);
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

    public void save(Bill bill) {
        billRepository.save(bill);
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
                id_business(request.getId_business()).
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

    public Timestamp getEarliestUpdateAt() {
        return billRepository.getEarliestUpdateAt();
    }

    public Timestamp getLatestUpdateAt() {
        return billRepository.getLatestUpdateAt();
    }

    //statistic
    public double getAllTotal(Integer state) {
        if (state != null) {
            return billRepository.getAllTotal(state);
        }
        return billRepository.getAllTotal();
    }

    public double getAllTotal(Timestamp startDate, Timestamp endDate, Integer state) {
        if (state != null) {
            return billRepository.getAllTotal(startDate, endDate, state);
        }
        return billRepository.getAllTotal(startDate, endDate);
    }

    public double getAllCount(Integer state) {
        if (state != null) {
            return billRepository.getAllCount(state);
        } else {
            return billRepository.getAllCount();
        }
    }

    public double getAllCount(Timestamp startDate, Timestamp endDate, Integer state) {
        if (state != null) {
            return billRepository.getAllCount(startDate, endDate, state);
        }
        return billRepository.getAllCount(startDate, endDate);
    }

    // Business

    public double getAllTotalBusiness(Integer state, Integer idBusiness) {
        if (state != null) {
            return billRepository.getAllTotalBusiness(state, idBusiness);
        }
        return billRepository.getAllTotalBusiness(idBusiness);
    }

    public double getAllTotalBusiness(Timestamp startDate, Timestamp endDate, Integer state, Integer idBusiness) {
        if (state != null) {
            return billRepository.getAllTotalBusiness(startDate, endDate, state, idBusiness);
        }
        return billRepository.getAllTotalBusiness(startDate, endDate, idBusiness);
    }

    public double getAllCountBusiness(Integer state, Integer idBusiness) {
        if (state != null) {
            return billRepository.getAllCountBusiness(state, idBusiness);
        }
        return billRepository.getAllCountBusiness(idBusiness);
    }

    public double getAllCountBusiness(Timestamp startDate, Timestamp endDate, Integer state, Integer idBusiness) {
        if (state != null) {
            return billRepository.getAllCountBusiness(startDate, endDate, state, idBusiness);
        }
        return billRepository.getAllCountBusiness(startDate, endDate, idBusiness);
    }

    //most

    public Page<ProductInfo> findMostSoldProducts(Pageable pageable, Integer state) {
        if (state != null) {
            return billRepository.findMostSoldProducts(pageable, state);
        }
        return billRepository.findMostSoldProducts(pageable);
    }

    public Page<ProductInfo> findMostSoldProductsBusiness(Pageable pageable, Integer id_business, Integer state) {
        if (state != null) {
            return billRepository.findMostSoldProductsBusiness(pageable, id_business, state);
        }
        return billRepository.findMostSoldProductsBusiness(pageable, id_business);
    }

}

