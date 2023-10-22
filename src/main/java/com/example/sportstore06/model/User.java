package com.example.sportstore06.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private Timestamp created_at;
    private Timestamp updated_at;
    @NotNull(message = "state must not be null")
    @Min(value = 0, message = "state must is (0,1,2)")
    @Max(value = 3, message = "state must is (0,1,2)")
    private Integer state;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_role")
    )
    @JsonManagedReference
    Set<Role> roleSet = new HashSet<>();

    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private Business business;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private Set<Comment> commentSet = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<Bill> billSet = new HashSet<>();

    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private Image image;

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roleSet.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}
