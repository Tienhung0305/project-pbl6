package com.example.sportstore06.service;


import com.example.sportstore06.dao.request.UserPutRequest;
import com.example.sportstore06.dao.request.UserRequest;
import com.example.sportstore06.entity.Role;
import com.example.sportstore06.entity.User;
import com.example.sportstore06.repository.IImageRepository;
import com.example.sportstore06.repository.IRoleRepository;
import com.example.sportstore06.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public Long getCount() {
        return userRepository.count();
    }
    public Optional<User> findById(int id) {
        return userRepository.findById(id);
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

    public Page<User> findByPage(Pageable pageable) {
        return userRepository.findByPage(pageable);
    }

    public Page<User> findByPage(Pageable pageable, Integer state) {
        return userRepository.findByPage(pageable, state);
    }

    public Page<User> SearchByName(Pageable pageable, String name) {
        return userRepository.SearchByName(pageable, name.toLowerCase());
    }

    public Page<User> SearchByName(Pageable pageable, String name, Integer state) {
        return userRepository.SearchByName(pageable, name.toLowerCase(), state);
    }

    public boolean deleteById(int id) {
        try {
            userRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
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
        Role role_business = roleRepository.findByName("ROLE_BUSINESS").get();
        Integer state = 0;
        if (roles.contains(role_business)) {
            state = 1;
        }
        var u = User.builder().
                id(id).
                name(request.getName()).
                dob(request.getDob()).
                email(request.getEmail()).
                phone(request.getPhone()).
                cic(request.getCic()).
                address(request.getAddress()).
                username(request.getUsername()).
                password(request.getPassword()).
                state(state).
                created_at(created_at).
                updated_at(updated_at).
                roleSet(roles).
                image_url(request.getImage_url()).
                build();
        userRepository.save(u);
    }

    public void save(int id, UserPutRequest request) {
        Timestamp created_at;
        Timestamp updated_at;
        if (userRepository.findById(id).isPresent()) {
            created_at = userRepository.findById(id).get().getCreated_at();
            updated_at = new Timestamp(new Date().getTime());
        } else {
            created_at = new Timestamp(new Date().getTime());
            updated_at = created_at;
        }

        User user = userRepository.findById(id).get();

        var u = User.builder().
                id(id).
                name(request.getName()).
                email(request.getEmail()).
                dob(request.getDob()).
                phone(request.getPhone()).
                cic(request.getCic()).
                address(request.getAddress()).
                username(user.getUsername()).
                password(passwordEncoder.encode(user.getPassword())).
                remember_token(user.getRemember_token()).
                revoked_token(user.getRevoked_token()).
                state(user.getState()).
                created_at(created_at).
                updated_at(updated_at).
                roleSet(user.getRoleSet()).
                image_url(request.getImage_url()).
                build();
        userRepository.save(u);
    }

    public void changeState(int id, int state) {
        User user = userRepository.findById(id).get();
        user.setState(state);
        userRepository.save(user);
    }
}
