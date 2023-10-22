package com.example.sportstore06.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "permissions")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String route;

    @ManyToMany(mappedBy = "permissionSet", fetch = FetchType.EAGER)
    @JsonBackReference
    private Set<Role> roleSet = new HashSet<>();
}