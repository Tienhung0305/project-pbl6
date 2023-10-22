package com.example.sportstore06.repository;

import com.example.sportstore06.model.BillDetail;
import com.example.sportstore06.model.Product;
import com.example.sportstore06.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IBillDetailRepository extends JpaRepository<BillDetail, Integer> {
    @Query("SELECT i FROM BillDetail i")
    Page<BillDetail> findByPage(Pageable pageable);
}
