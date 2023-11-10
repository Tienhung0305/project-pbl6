package com.example.sportstore06.repository;

import com.example.sportstore06.model.Role;
import com.example.sportstore06.model.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role,Integer> {
    Optional<Role> findByName(String name);
    @Query("SELECT i FROM Role i")
    Page<Role> findByPage(Pageable pageable);

}
