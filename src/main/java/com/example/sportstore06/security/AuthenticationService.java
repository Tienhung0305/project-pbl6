package com.example.sportstore06.security;

import com.example.sportstore06.dao.response.JwtAuthenticationResponse;
import com.example.sportstore06.dao.request.SignUpRequest;
import com.example.sportstore06.dao.request.SignInRequest;
public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SignInRequest request);
}
