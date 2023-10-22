package com.example.sportstore06.repository;

import com.example.sportstore06.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role,Integer> {
    Optional<Role> findByName(String name);

}
