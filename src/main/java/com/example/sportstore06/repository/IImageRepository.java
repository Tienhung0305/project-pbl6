package com.example.sportstore06.repository;

import com.example.sportstore06.model.Image;
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
public interface IImageRepository extends JpaRepository<Image,Integer> {
    @Query("SELECT i FROM Image i")
    Page<Image> findByPage(Pageable pageable);

    List<Image> findByName(String Name);
}
