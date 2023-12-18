package com.example.sportstore06.repository;

import com.example.sportstore06.model.Product;
import com.example.sportstore06.model.ProductInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IProductRepository extends JpaRepository<Product,Integer> {
    @Query("SELECT i FROM Product i WHERE i.productInfo.id = :id_product_information")
    List<Product> getByProductIf(Integer id_product_information);
}
