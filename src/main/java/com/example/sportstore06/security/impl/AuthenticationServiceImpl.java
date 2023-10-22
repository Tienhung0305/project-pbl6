package com.example.sportstore06.security.impl;

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
    public JwtAuthenticationResponse signup(SignUpRequest request) {
        Set<Role> roles = new HashSet<>();
        for (String i : request.getRoles()) {
            Optional<Role> ObRole = roleRepository.findByName(i);
            if (ObRole.isPresent()) {
                roles.add(ObRole.get());
            } else {
                return null;
            }
        }
        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roleSet(roles).build();
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
