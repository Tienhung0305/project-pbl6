package com.example.sportstore06.service;

import com.example.sportstore06.dao.request.CategoryRequest;
import com.example.sportstore06.entity.Category;
import com.example.sportstore06.repository.ICategoryGroupRepository;
import com.example.sportstore06.repository.ICategoryRepository;
import com.example.sportstore06.repository.IImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final ICategoryRepository categoryRepository;
    private final IImageRepository imageRepository;
    private final ICategoryGroupRepository categoryGroupRepository;

    public Long getCount() {
        return categoryRepository.count();
    }
    public Optional<Category> findById(int id) {
        return categoryRepository.findById(id);
    }

    public Page<Category> SearchByName(Pageable pageable, String name) {
        return categoryRepository.SearchByName(pageable, name);
    }

    public Page<Category> findByPage(Pageable pageable) {
        return categoryRepository.findByPage(pageable);
    }

    public boolean deleteById(int id) {
        try {
            categoryRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void save(int id, CategoryRequest request) {

        var c = Category.builder().
                id(id).
                name(request.getName()).
                categoryGroup(categoryGroupRepository
                        .findById(request.getCategory_group_id())
                        .get()).
                build();
        categoryRepository.save(c);
    }
}
