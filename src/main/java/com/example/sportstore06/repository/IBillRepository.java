package com.example.sportstore06.repository;

import com.example.sportstore06.model.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBillRepository extends JpaRepository<Bill, Integer> {
    @Query("SELECT i FROM Bill i")
    Page<Bill> findByPage(Pageable pageable);

    @Query("SELECT i FROM Bill i WHERE i.state = :state")
    Page<Bill> findByPage(Pageable pageable, Integer state);

    @Query("SELECT i FROM Bill i WHERE i.name LIKE %:Name%")
    Page<Bill> SearchByName(Pageable pageable, String Name);

    @Query("SELECT i FROM Bill i WHERE i.name LIKE %:Name% AND i.state = :state")
    Page<Bill> SearchByName(Pageable pageable, String Name, Integer state);
    @Query("SELECT i FROM Bill i WHERE i.user.id = :id_user")
    List<Bill> findByIdUser(Integer id_user);
}
