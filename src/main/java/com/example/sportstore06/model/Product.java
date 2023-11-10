package com.example.sportstore06.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank
    @Size(min = 1, max = 100)
    private String name;
    private String detail;
    @Min(value = 0)
    private Double price;
    private String attribute;
    private String brand;
    @Min(value = 0)
    private Integer quantity;
    //private int id_business;
    //private int id_sale;
    //private int id_category;
    private Timestamp created_at;
    private Timestamp updated_at;
    @NotNull
    @Min(value = 0)
    @Max(value = 3)
    private Integer state;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_business", nullable = false, referencedColumnName = "id")
    @JsonBackReference
    private Business business;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_category", referencedColumnName = "id")
    @JsonBackReference
    private Category category;

    @OneToOne(mappedBy = "product", fetch = FetchType.EAGER)
    @JsonManagedReference
    private BillDetail bill_detail;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_sale", referencedColumnName = "id")
    @JsonBackReference
    private Sale sale;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private Set<Image> imageSet = new HashSet<>();
}
