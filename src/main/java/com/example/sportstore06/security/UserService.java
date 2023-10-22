package com.example.sportstore06.security;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    UserDetailsService userDetailsService();
}
