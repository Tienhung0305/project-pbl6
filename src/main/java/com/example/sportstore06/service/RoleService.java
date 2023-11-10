package com.example.sportstore06.service;

import com.example.sportstore06.dao.request.RoleRequest;
import com.example.sportstore06.dao.request.UserRequest;
import com.example.sportstore06.model.Role;
import com.example.sportstore06.model.Sale;
import com.example.sportstore06.repository.IRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final IRoleRepository roleRepository;
    public Optional<Role> findById(int id) {
        return roleRepository.findById(id);
    }
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    };
    public Page<Role> findByPage(Pageable pageable) {
        return roleRepository.findByPage(pageable);
    }
    public void save(int id, RoleRequest request) {
        var r = Role.builder().
                id(id).
                name(request.getName()).
                build();
        roleRepository.save(r);
    }
}
