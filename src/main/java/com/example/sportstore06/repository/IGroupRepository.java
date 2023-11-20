package com.example.sportstore06.repository;

import com.example.sportstore06.model.Category;
import com.example.sportstore06.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IGroupRepository  extends JpaRepository<Group,Integer> {

}
