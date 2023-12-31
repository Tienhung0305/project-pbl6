package com.example.sportstore06.security.impl;

import com.example.sportstore06.dao.request.SignInRequest;
import com.example.sportstore06.dao.request.SignUpBusinessRequest;
import com.example.sportstore06.dao.request.SignUpCustomerRequest;
import com.example.sportstore06.dao.response.JwtAuthenticationResponse;
import com.example.sportstore06.entity.Business;
import com.example.sportstore06.entity.Role;
import com.example.sportstore06.entity.User;
import com.example.sportstore06.repository.IBusinessRepository;
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
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final IUserRepository userRepository;
    private final IBusinessRepository businessRepository;
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
                .state(1)
                .build();
        var jwt = jwtService.generateToken(user);
        user.setRemember_token(jwt);
        user.setRevoked_token(false);
        User save = userRepository.save(user);

        UUID uuid = UUID.randomUUID();
        Business business = new Business();
        business.setId(save.getId());
        business.setName(request.getName());
        business.setRevenue(0L);
        business.setPayment(uuid.toString());
        businessRepository.save(business);

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
        var jwt = jwtService.generateToken(user);
        user.setRemember_token(jwt);
        user.setRevoked_token(false);
        userRepository.save(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    @Override
    public JwtAuthenticationResponse signin(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password."));
        var jwt = jwtService.generateToken(user);
        user.setRemember_token(jwt);
        user.setRevoked_token(false);
        userRepository.save(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }
}
