package com.example.sportstore06.entity;

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
    private Double discount;
    //private Integer id_business;
    @NotNull
    private Timestamp started_at;
    @NotNull
    private Timestamp ended_at;
    @NotBlank
    private String name;
    private String content;
    private String url;

    @OneToMany(mappedBy = "sale", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<ProductInfo> productInfoSet = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_business", nullable = false, referencedColumnName = "id")
    @JsonBackReference
    private Business business;
}
