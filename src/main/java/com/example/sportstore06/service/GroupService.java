package com.example.sportstore06.service;

import com.example.sportstore06.model.Category;
import com.example.sportstore06.model.Group;
import com.example.sportstore06.repository.ICategoryRepository;
import com.example.sportstore06.repository.IGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final IGroupRepository groupRepository;
    public Optional<Group> findById(int id) {
        return groupRepository.findById(id);
    }

    public List<Group> findAll() {
        return groupRepository.findAll();
    }
}
