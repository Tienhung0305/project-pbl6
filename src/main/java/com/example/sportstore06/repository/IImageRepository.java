package com.example.sportstore06.repository;

import com.example.sportstore06.entity.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IImageRepository extends JpaRepository<Image,Integer> {
    @Query("SELECT i FROM Image i")
    Page<Image> findByPage(Pageable pageable);
    @Query("SELECT i FROM Image i WHERE i.name LIKE %:Name%")
    Page<Image> SearchByName(Pageable pageable, String Name);
}
