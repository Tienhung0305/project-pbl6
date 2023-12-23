package com.example.sportstore06.service;

import com.example.sportstore06.entity.CategoryGroup;
import com.example.sportstore06.repository.ICategoryGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryGroupService {
    private final ICategoryGroupRepository categoryGroupRepository;
    public Optional<CategoryGroup> findById(int id) {
        return categoryGroupRepository.findById(id);
    }
    public List<CategoryGroup> findAll() {
        return categoryGroupRepository.findAll();
    }
}
