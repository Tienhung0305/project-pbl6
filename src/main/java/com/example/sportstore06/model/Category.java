package com.example.sportstore06.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private Timestamp created_at;
    private Timestamp updated_at;

    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<Product> productSet = new HashSet<>();

    @OneToOne(mappedBy = "category", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private Image image;
}
