package com.example.sportstore06.repository;

import com.example.sportstore06.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProductRepository extends JpaRepository<Product,Integer> {
}
