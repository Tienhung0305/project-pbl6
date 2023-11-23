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
    @Query("SELECT i FROM Product i JOIN i.categorySet c WHERE i.state = :state AND c.id IN (:categoryIds)")
    Page<Product> findByCategory(Pageable pageable, Integer state, List<Integer> categoryIds);
    @Query("SELECT i FROM Product i JOIN i.categorySet c WHERE c.id IN (:categoryIds)")
    Page<Product> findByCategory(Pageable pageable, List<Integer> categoryIds);
    @Query("SELECT i FROM Product i WHERE i.state = :state AND i.business.id = :id_business")
    Page<Product> findByBusiness(Pageable pageable, Integer state, Integer id_business);
    @Query("SELECT i FROM Product i WHERE i.business.id = :id_business")
    Page<Product> findByBusiness(Pageable pageable, Integer id_business);
    @Query("SELECT i FROM Product i WHERE i.state = :state AND i.sale.id = :id_sale")
    Page<Product> findBySale(Pageable pageable, Integer state, Integer id_sale);
    @Query("SELECT i FROM Product i WHERE i.sale.id = :id_sale")
    Page<Product> findBySale(Pageable pageable, Integer id_sale);
    @Query("SELECT i FROM Product i WHERE i.name LIKE %:Name%")
    Page<Product>SearchByName(Pageable pageable,String Name);
    @Query("SELECT i FROM Product i WHERE i.name LIKE %:Name% AND i.state = :state")
    Page<Product>SearchByName(Pageable pageable, String Name, Integer state);
}
