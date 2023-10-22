package com.example.sportstore06.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_permissions",
            joinColumns = @JoinColumn(name = "id_role"),
            inverseJoinColumns = @JoinColumn(name = "id_permission"))
    @JsonManagedReference
    private Set<Permission> permissionSet = new HashSet<>();

    @ManyToMany(mappedBy = "roleSet", fetch = FetchType.EAGER)
    @JsonBackReference
    private Set<User> userSet = new HashSet<>();
}
