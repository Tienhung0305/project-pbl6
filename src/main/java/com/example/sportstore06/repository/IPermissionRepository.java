package com.example.sportstore06.repository;

import com.example.sportstore06.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPermissionRepository extends JpaRepository<Permission,Integer> {
}
