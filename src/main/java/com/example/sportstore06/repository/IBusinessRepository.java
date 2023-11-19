package com.example.sportstore06.repository;

import com.example.sportstore06.model.Business;
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
public interface IBusinessRepository extends JpaRepository<Business, Integer> {
    @Query("SELECT i FROM Business i")
    Page<Business> findByPage(Pageable pageable);
    @Query("SELECT i FROM Business i WHERE i.user.state = :state")
    Page<Business> findByPage(Pageable pageable, Integer state);
    @Query("SELECT i FROM Business i WHERE i.name LIKE %:Name%")
    List<Business> SearchByName (String Name);
    @Query("SELECT i FROM Business i WHERE i.name LIKE %:Name% AND i.user.state = :state")
    List<Business> SearchByName (String Name, Integer state);
    Optional<Business>  findByName (String Name);
}
