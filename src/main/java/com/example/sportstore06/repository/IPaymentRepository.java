package com.example.sportstore06.repository;

import com.example.sportstore06.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IPaymentRepository extends JpaRepository<Payment,String> {
    @Query("SELECT i FROM Payment i")
    Page<Payment> findByPage(Pageable pageable);
}
