package com.example.sportstore06.repository;

import com.example.sportstore06.model.Sale;
import com.example.sportstore06.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ISaleRepository extends JpaRepository<Sale,Integer> {
    @Query("SELECT i FROM Sale i")
    Page<Sale> findByPage(Pageable pageable);

    List<Sale> findByName(String Name);
}
