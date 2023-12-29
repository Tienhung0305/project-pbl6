package com.example.sportstore06.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "business")
public class Business {
    @Id
    private int id;
    @Column(unique = true)
    @NotBlank
    @Size(min = 1, max = 50)
    private String name;
    private String about;
    @Column(unique = true)
    @NotBlank(message = "payment must not be blank")
    private String payment;
    private Integer tax;

    @OneToMany(mappedBy = "business", fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<ProductInfo> productInfoSet = new HashSet<>();

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id", referencedColumnName = "id")
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "business", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private Set<Sale> saleSet = new HashSet<>();

}
