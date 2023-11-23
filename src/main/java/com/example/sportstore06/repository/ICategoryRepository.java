package com.example.sportstore06.repository;

import com.example.sportstore06.model.Category;
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
public interface ICategoryRepository extends JpaRepository<Category,Integer> {
    @Query("SELECT i FROM Category i")
    Page<Category> findByPage(Pageable pageable);
    @Query("SELECT i FROM Category i")
    List<Category> findByGroupName(Pageable pageable);
    @Query("SELECT i FROM Category i WHERE i.name LIKE %:Name%")
    Page<Category> SearchByName(Pageable pageable, String Name);
}
