package com.example.sportstore06.entity;

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
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    //private int category_group_id;

    @ManyToMany(mappedBy = "categorySet", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<ProductInfo> productInfoSet = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_group_id", nullable = false, referencedColumnName = "id")
    @JsonBackReference
    private CategoryGroup categoryGroup;

}
