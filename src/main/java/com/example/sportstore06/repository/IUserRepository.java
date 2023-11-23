package com.example.sportstore06.repository;


import com.example.sportstore06.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByUsername(String userName);
    @Query("SELECT i FROM User i WHERE i.name LIKE %:Name%")
    Page<User> SearchByName(Pageable pageable, String Name);
    @Query("SELECT i FROM User i WHERE i.name LIKE %:Name% AND i.state = :state")
    Page<User> SearchByName(Pageable pageable, String Name, Integer state);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
    Optional<User> findByCic(String cic);
    @Query("SELECT i FROM User i")
    Page<User> findByPage(Pageable pageable);
    @Query("SELECT i FROM User i WHERE i.state = :state")
    Page<User> findByPage(Pageable pageable, Integer state);
}
