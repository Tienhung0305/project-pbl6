package com.example.sportstore06.dao.request;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @Size(min = 1, max = 50, message = "name must be between 1 and 50 characters")
    private String name;
    @Size(min = 5, max = 50, message = "email must be between 5 and 50 characters")
    @Email(message = "email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;
    private Timestamp dob;
    @Size(min = 7, max = 11, message = "phone number must be between 7 and 11 characters")
    private String phone;
    @Size(min = 10, max = 12, message = "citizen identification card must be between 10 and 12 characters")
    private String cic;
    @Size(min = 5, max = 300, message = "address must be between 5 and 300 characters")
    private String address;
    @Size(min = 1, max = 50, message = "username must be between 1 and 50 characters")
    private String username;
    @Size(min = 1, max = 100, message = "password must be between 1 and 100 characters")
    private String password;
    private Timestamp created_at;
    private Timestamp updated_at;
    private String image_url;

    @NotNull(message = "roles must not be null")
    private Set<String> roles;
}
