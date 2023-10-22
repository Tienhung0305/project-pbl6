package com.example.sportstore06.service;

import com.example.sportstore06.dao.request.BusinessRequest;
import com.example.sportstore06.dao.response.BusinessResponse;
import com.example.sportstore06.dao.response.ProductResponse;
import com.example.sportstore06.model.Business;
import com.example.sportstore06.model.Product;
import com.example.sportstore06.repository.IBusinessRepository;
import com.example.sportstore06.repository.IImageRepository;
import com.example.sportstore06.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
public class BusinessService {
    private final IBusinessRepository businessRepository;
    private final IUserRepository userRepository;
    private final IImageRepository iImageRepository;
    public List<Business> findAll() {
        return businessRepository.findAll();
    }

    public ResponseEntity<?> findByPage(Pageable pageable) {
        Page<Business> byPage = businessRepository.findByPage(pageable);
        Page<BusinessResponse> responses = byPage.map(business -> new BusinessResponse(business));
        return ResponseEntity.ok().body(responses);
    }

    public ResponseEntity<?> findById(int id) {
        if (businessRepository.findById(id).isPresent()) {
            return ResponseEntity.ok(businessRepository.findById(id).map(business -> new BusinessResponse(business)).get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id business not found");
    }

    public ResponseEntity<?> save(BusinessRequest request) {
        try {
            if (userRepository.findById(request.getId_user()).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id user not found ");
            } else if (request.getState() < 0 || request.getState() > 3) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("state not found");
            } else if (iImageRepository.findById(request.getId_image()).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id image not found");
            } else {
                Timestamp created_at;
                Timestamp updated_at;
                if (businessRepository.findById(request.getId_user()).isPresent()) {
                    created_at = businessRepository.findById(request.getId_user()).get().getCreated_at();
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
                return ResponseEntity.accepted().build();
            }
        } catch (
                Exception exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    public ResponseEntity<?> changeState(Integer id, Integer state) {
        if (businessRepository.findById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id business not found");
        } else if (state < 0 || state > 3) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("state not found");
        } else {
            Business business = businessRepository.findById(id).get();
            business.setState(state);
            businessRepository.save(business);
            return ResponseEntity.accepted().build();
        }
    }

    public ResponseEntity<?> deleteById(int id) {
        try {
            if (businessRepository.findById(id).isPresent()) {
                businessRepository.deleteById(id);
                return ResponseEntity.accepted().build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id business not found");
            }
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }
}
