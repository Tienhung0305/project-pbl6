package com.example.sportstore06.service;


import com.example.sportstore06.dao.request.UserRequest;
import com.example.sportstore06.model.Business;
import com.example.sportstore06.model.Image;
import com.example.sportstore06.model.Role;
import com.example.sportstore06.model.User;
import com.example.sportstore06.repository.IImageRepository;
import com.example.sportstore06.repository.IRoleRepository;
import com.example.sportstore06.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final IImageRepository iImageRepository;

    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }
    public List<User> findByName(String name) {
        return userRepository.findByName(name);
    }
    public Page<User> findByPage(Pageable pageable) {
        return userRepository.findByPage(pageable);
    }

    public boolean deleteById(int id) {
        try {
            userRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public Optional<User> findByUsername(String userName) {
        return userRepository.findByUsername(userName);
    }
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public Optional<User> findByPhone(String phone_number) {
        return userRepository.findByPhone(phone_number);
    }
    public Optional<User> findByCic(String cic) {
        return userRepository.findByCic(cic);
    }

    public void save(int id, UserRequest request) {
        Timestamp created_at;
        Timestamp updated_at;
        if (userRepository.findById(id).isPresent()) {
            created_at = userRepository.findById(id).get().getCreated_at();
            updated_at = new Timestamp(new Date().getTime());
        } else {
            created_at = new Timestamp(new Date().getTime());
            updated_at = created_at;
        }

        Set<Role> roles = new HashSet<>();
        for (String i : request.getRoles()) {
            Optional<Role> ObRole = roleRepository.findByName(i);
            roles.add(ObRole.get());
        }

        Image image = iImageRepository.findById(request.getId_image()).get();

        var u = User.builder().
                id(id).
                name(request.getName()).
                email(request.getEmail()).
                dob(request.getDob()).
                phone(request.getPhone()).
                cic(request.getCic()).
                address(request.getAddress()).
                username(request.getUsername()).
                password(request.getPassword()).
                state(request.getState()).
                remember_token(request.getRemember_token()).
                created_at(created_at).
                updated_at(updated_at).
                roleSet(roles).
                image(image).
                build();
        userRepository.save(u);
    }

    public void changeState(int id, int state) {
        User user = userRepository.findById(id).get();
        user.setState(state);
        userRepository.save(user);
    }
}
