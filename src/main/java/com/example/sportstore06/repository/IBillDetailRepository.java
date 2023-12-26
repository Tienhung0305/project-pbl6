package com.example.sportstore06.repository;

import com.example.sportstore06.entity.BillDetail;
import com.example.sportstore06.entity.ProductInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBillDetailRepository extends JpaRepository<BillDetail, Integer> {
    @Query("SELECT i FROM BillDetail i")
    Page<BillDetail> findByPage(Pageable pageable);

}
