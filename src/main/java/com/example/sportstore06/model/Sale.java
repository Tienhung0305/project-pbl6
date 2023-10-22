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
@Table(name = "sales")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private Timestamp started_at;
    @Column(nullable = false)
    private Timestamp ended_at;
    @Column(nullable = false)
    private String name;
    private String content;
    @OneToMany(mappedBy = "sale", fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<Product> productSet = new HashSet<>();
}
