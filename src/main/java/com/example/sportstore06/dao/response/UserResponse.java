package com.example.sportstore06.dao.response;

import com.example.sportstore06.model.Image;
import com.example.sportstore06.model.User;
import lombok.*;

import jakarta.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private int id;
    private String name;
    private String email;
    private String username;
    private String password;
    private String remember_token;
    private Set<String> roles;
    private Image image;
    private Timestamp created_at;
    private Timestamp updated_at;
    private Integer state;

    public UserResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.state = user.getState();
        this.remember_token = user.getRemember_token();
        this.created_at = user.getCreated_at();
        this.updated_at = user.getUpdated_at();
        this.image = user.getImage();
        this.roles = user.getRoleSet()
                .stream()
                .map(role -> role.getName())
                .collect(Collectors.toSet());
    }
}
