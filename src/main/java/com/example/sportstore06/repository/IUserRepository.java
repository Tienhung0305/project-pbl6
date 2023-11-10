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
    List<User> findByName(String Name);
    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String email);
    Optional<User> findByCic(String email);
    @Query("SELECT i FROM User i")
    Page<User> findByPage(Pageable pageable);
}
