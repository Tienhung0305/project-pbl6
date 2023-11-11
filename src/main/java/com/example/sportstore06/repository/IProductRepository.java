package com.example.sportstore06.repository;

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
public interface IProductRepository extends JpaRepository<Product,Integer> {
    @Query("SELECT i FROM Product i")
    Page<Product> findByPage(Pageable pageable);
    @Query("SELECT i FROM Product i WHERE i.state = :state")
    Page<Product> findByPage(Pageable pageable, Integer state);
    @Query("SELECT i FROM Product i WHERE i.name LIKE %:Name%")
    List<Product>SearchByName(String Name);
    @Query("SELECT i FROM Product i WHERE i.name LIKE %:Name% AND i.state = :state")
    List<Product>SearchByName(String Name, Integer state);
}
