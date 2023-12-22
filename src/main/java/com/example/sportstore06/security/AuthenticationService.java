package com.example.sportstore06.security;

import com.example.sportstore06.dao.request.SignUpBusinessRequest;
import com.example.sportstore06.dao.request.SignUpCustomerRequest;
import com.example.sportstore06.dao.response.JwtAuthenticationResponse;
import com.example.sportstore06.dao.request.SignUpRequest;
import com.example.sportstore06.dao.request.SignInRequest;
public interface AuthenticationService {
    JwtAuthenticationResponse signupCustomer(SignUpCustomerRequest request);
    JwtAuthenticationResponse signUpBusiness(SignUpBusinessRequest request);
    JwtAuthenticationResponse signin(SignInRequest request);
}
