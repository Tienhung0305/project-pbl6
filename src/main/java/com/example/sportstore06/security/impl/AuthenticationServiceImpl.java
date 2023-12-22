package com.example.sportstore06.security.impl;

import com.example.sportstore06.dao.request.SignUpBusinessRequest;
import com.example.sportstore06.dao.request.SignUpCustomerRequest;
import com.example.sportstore06.dao.response.JwtAuthenticationResponse;
import com.example.sportstore06.dao.request.SignUpRequest;
import com.example.sportstore06.dao.request.SignInRequest;
import com.example.sportstore06.model.Role;
import com.example.sportstore06.model.User;
import com.example.sportstore06.repository.IRoleRepository;
import com.example.sportstore06.repository.IUserRepository;
import com.example.sportstore06.security.AuthenticationService;
import com.example.sportstore06.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final IRoleRepository roleRepository;

    @Override
    public JwtAuthenticationResponse signUpBusiness(SignUpBusinessRequest request) {
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("ROLE_BUSINESS").get());
        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roleSet(roles)
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .cic(request.getCic())
                .address(request.getAddress())
                .state(1).
                 build();
        userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    @Override
    public JwtAuthenticationResponse signupCustomer(SignUpCustomerRequest request) {
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("ROLE_CUSTOMER").get());
        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roleSet(roles)
                .email(request.getEmail())
                .name(request.getName())
                .phone(request.getPhone())
                .state(0).
                build();
        userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    @Override
    public JwtAuthenticationResponse signin(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password."));
        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }
}
