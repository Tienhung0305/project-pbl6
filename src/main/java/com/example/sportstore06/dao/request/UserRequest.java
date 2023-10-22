package com.example.sportstore06.dao.request;


import com.example.sportstore06.model.Image;
import jakarta.validation.constraints.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private int id;
    @NotBlank(message = "name must not be blank")
    @Size(min = 1, max = 50, message = "name must be between 1 and 50 characters")
    private String name;
    @NotBlank(message = "email must not be blank")
    @Size(min = 5, max = 50, message = "email must be between 5 and 50 characters")
    @Email(message = "email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;
    @NotBlank(message = "username must not be blank")
    @Size(min = 1, max = 50, message = "username must be between 1 and 50 characters")
    private String username;
    @NotBlank(message = "password must not be blank")
    @Size(min = 1, max = 50, message = "password must be between 1 and 50 characters")
    private String password;
    private String remember_token;
    @NotNull(message = "roles must not be null")
    private Set<String> roles;
    private Integer id_image;
    private Timestamp created_at;
    private Timestamp updated_at;
    @NotNull(message = "state must not be null")
    @Min(value = 0, message = "state must is (0,1,2)")
    @Max(value = 3, message = "state must is (0,1,2)")
    private Integer state;
}
