package com.example.sportstore06.security;
import org.springframework.security.core.userdetails.UserDetails;
public interface JwtService {
    String extractUserName(String token);

    String generateToken(UserDetails userDetails);

    boolean isTokenValid(String token, UserDetails userDetails);
}
