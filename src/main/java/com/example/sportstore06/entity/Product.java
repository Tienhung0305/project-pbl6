package com.example.sportstore06.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    //private Integer id_product_information;
    @Min(value = 1)
    private Double price;
    @Min(value = 1)
    private Integer quantity;
    private String size;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "id_product_information", nullable = false, referencedColumnName = "id")
    @JsonBackReference
    private ProductInfo productInfo;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    @JsonBackReference
    private Set<Cart> cartSet = new HashSet<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<BillDetail> billDetailSet = new HashSet<>();
}
