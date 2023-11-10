package com.example.sportstore06.service;


import com.example.sportstore06.dao.request.BusinessRequest;
import com.example.sportstore06.model.Bill;
import com.example.sportstore06.model.Business;
import com.example.sportstore06.model.Product;
import com.example.sportstore06.repository.IBusinessRepository;
import com.example.sportstore06.repository.IImageRepository;
import com.example.sportstore06.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BusinessService {
    private final IBusinessRepository businessRepository;
    private final IImageRepository iImageRepository;
    public Optional<Business> findById(int id) {
        return businessRepository.findById(id);
    }

    public List<Business> findByName(String name) {
        return businessRepository.findByName(name);
    }

    public Page<Business> findByPage(Pageable pageable) {
        return businessRepository.findByPage(pageable);
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
        Timestamp created_at;
        Timestamp updated_at;
        if (businessRepository.findById(id).isPresent()) {
            created_at = businessRepository.findById(id).get().getCreated_at();
            updated_at = new Timestamp(new Date().getTime());
        } else {
            created_at = new Timestamp(new Date().getTime());
            updated_at = created_at;
        }
        var b = Business.builder().
                id(request.getId_user()).
                name(request.getName()).
                about(request.getAbout()).
                tax(request.getTax()).
                state(request.getState()).
                created_at(created_at).
                updated_at(updated_at).
                image(iImageRepository.findById(request.getId_image()).get()).
                build();
        businessRepository.save(b);
    }

    public void changeState(int id, int state) {
        Business business = businessRepository.findById(id).get();
        business.setState(state);
        businessRepository.save(business);
    }
}
