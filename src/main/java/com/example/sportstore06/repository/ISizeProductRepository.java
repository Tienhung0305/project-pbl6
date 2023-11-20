package com.example.sportstore06.repository;

import com.example.sportstore06.model.Sale;
import com.example.sportstore06.model.SizeProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISizeProductRepository extends JpaRepository<SizeProduct,Integer> {
}
