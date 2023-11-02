package com.example.sportstore06.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private Integer id_business;
    @NotNull
    private Timestamp started_at;
    @NotNull
    private Timestamp ended_at;
    @NotBlank
    private String name;
    private String content;

    @OneToMany(mappedBy = "sale", fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<Product> productSet = new HashSet<>();

    @OneToOne(mappedBy = "sale", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Image image;
}
