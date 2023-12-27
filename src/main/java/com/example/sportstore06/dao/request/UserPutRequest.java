package com.example.sportstore06.dao.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPutRequest {
    @NotBlank(message = "name must not be blank")
    @Size(min = 1, max = 50, message = "name must be between 1 and 50 characters")
    private String name;
    @NotBlank(message = "email must not be blank")
    @Size(min = 5, max = 50, message = "email must be between 5 and 50 characters")
    @Email(message = "email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;
    private Timestamp dob;
    @NotBlank(message = "phone number must not be blank")
    @Size(min = 7, max = 11, message = "phone number must be between 7 and 11 characters")
    private String phone;
    @NotBlank(message = "citizen identification card must not be blank")
    @Size(min = 10, max = 12, message = "citizen identification card must be between 10 and 12 characters")
    private String cic;
    @NotBlank(message = "address must not be blank")
    @Size(min = 5, max = 300, message = "address must be between 5 and 300 characters")
    private String address;
    private Timestamp created_at;
    private Timestamp updated_at;
    private String image_url;
}
