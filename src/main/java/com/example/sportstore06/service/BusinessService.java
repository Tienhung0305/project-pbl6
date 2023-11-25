package com.example.sportstore06.service;


import com.example.sportstore06.dao.request.BusinessRequest;
import com.example.sportstore06.model.Business;
import com.example.sportstore06.repository.IBusinessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BusinessService {
    private final IBusinessRepository businessRepository;
    public Long getCount() {
        return businessRepository.count();
    }
    public Optional<Business> findById(int id) {
        return businessRepository.findById(id);
    }

    public Optional<Business> findByName(String name) {
        return businessRepository.findByName(name);
    }

    public Page<Business> SearchByName(Pageable pageable, String name) {
        return businessRepository.SearchByName(pageable,name);
    }

    public Page<Business> findByPage(Pageable pageable) {
        return businessRepository.findByPage(pageable);
    }

    public Page<Business> SearchByName(Pageable pageable, String name, Integer state) {
        return businessRepository.SearchByName(pageable,name,state);
    }
    public Page<Business> findByPage(Pageable pageable, Integer state) {
        return businessRepository.findByPage(pageable,state);
    }

    public boolean deleteById(int id) {
        try {
            businessRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public void save(int id, BusinessRequest request) {
        var b = Business.builder().
                id(request.getId_user()).
                name(request.getName()).
                about(request.getAbout()).
                tax(request.getTax()).
                payment(request.getPayment()).
                build();
        businessRepository.save(b);
    }

}
