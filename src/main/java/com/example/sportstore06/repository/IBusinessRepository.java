package com.example.sportstore06.repository;

import com.example.sportstore06.model.Business;
import com.example.sportstore06.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IBusinessRepository extends JpaRepository<Business, Integer> {
    @Query("SELECT i FROM Business i")
    Page<Business> findByPage(Pageable pageable);
}
