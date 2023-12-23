package com.example.sportstore06.dao.response;

import com.example.sportstore06.entity.User;
import lombok.*;

import java.sql.Timestamp;
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
    private String image_url;
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
        this.image_url = user.getImage_url();
        this.roles = user.getRoleSet()
                .stream()
                .map(role -> role != null ? role.getName() : null)
                .collect(Collectors.toSet());
    }
}
