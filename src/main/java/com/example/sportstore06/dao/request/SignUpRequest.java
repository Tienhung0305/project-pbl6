package com.example.sportstore06.dao.request;

import com.example.sportstore06.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    @NotBlank(message = "username must not be blank")
    @Size(min = 1, max = 50, message = "username must be between 1 and 50 characters")
    private String username;
    @NotBlank(message = "password must not be blank")
    @Size(min = 1, max = 100, message = "password must be between 1 and 100 characters")
    private String password;
    private Set<String> roles;
    private String name;
    @NotBlank(message = "email must not be blank")
    @Size(min = 5, max = 50, message = "email must be between 5 and 50 characters")
    @Email(message = "email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;
    @NotBlank(message = "phone number must not be blank")
    @Size(min = 7, max = 11, message = "phone number must be between 7 and 11 characters")
    private String phone;
    @NotBlank(message = "citizen identification card must not be blank")
    @Size(min = 10, max = 12, message = "citizen identification card must be between 10 and 12 characters")
    private String cic;
    @NotBlank(message = "address must not be blank")
    @Size(min = 5, max = 300, message = "address must be between 5 and 300 characters")
    private String address;
}