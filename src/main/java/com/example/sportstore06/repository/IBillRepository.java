package com.example.sportstore06.repository;

import com.example.sportstore06.model.Bill;
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
public interface IBillRepository extends JpaRepository<Bill, Integer> {
    @Query("SELECT i FROM Bill i")
    Page<Bill> findByPage(Pageable pageable);

    List<Bill> findByName(String Name);
}
