package com.example.sportstore06.repository;

import com.example.sportstore06.model.Payment;
import com.example.sportstore06.model.Product;
import com.example.sportstore06.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IPaymentRepository extends JpaRepository<Payment,Integer> {
    @Query("SELECT i FROM Payment i")
    Page<Payment> findByPage(Pageable pageable);

   List<Payment> findByName(String Name);
}
