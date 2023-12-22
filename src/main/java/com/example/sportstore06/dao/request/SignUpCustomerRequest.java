package com.example.sportstore06.dao.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpCustomerRequest {
    @NotBlank(message = "username must not be blank")
    @Size(min = 1, max = 50, message = "username must be between 1 and 50 characters")
    private String username;
    @NotBlank(message = "password must not be blank")
    @Size(min = 1, max = 100, message = "password must be between 1 and 100 characters")
    private String password;
    @NotBlank(message = "name must not be blank")
    @Size(min = 5, max = 50, message = "name must be between 5 and 50 characters")
    private String name;
    @NotBlank(message = "phone number must not be blank")
    @Size(min = 7, max = 11, message = "phone number must be between 7 and 11 characters")
    private String phone;
    @NotBlank(message = "email must not be blank")
    @Size(min = 5, max = 50, message = "email must be between 5 and 50 characters")
    @Email(message = "email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;
}
