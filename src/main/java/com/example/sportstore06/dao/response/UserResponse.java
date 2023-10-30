package com.example.sportstore06.dao.response;

import com.example.sportstore06.model.Image;
import com.example.sportstore06.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Timestamp dob;
    private String phone;
    private String cic;
    private String address;
    private String username;
    private Set<String> roles;
    private Image image;
    private Timestamp created_at;
    private Timestamp updated_at;
    private Integer state;

    public UserResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.dob = user.getDob();
        this.phone = user.getPhone();
        this.cic = user.getCic();
        this.address = user.getAddress();
        this.username = user.getUsername();
        this.state = user.getState();
        this.created_at = user.getCreated_at();
        this.updated_at = user.getUpdated_at();
        this.image = user.getImage();
        this.roles = user.getRoleSet()
                .stream()
                .map(role -> role.getName())
                .collect(Collectors.toSet());
    }
}
