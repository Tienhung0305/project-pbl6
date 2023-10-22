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
    @NotBlank(message = "name must not be blank")
    @Size(min = 1, max = 100, message = "name must be between 1 and 100 characters")
    private String name;
    @Positive(message = "id must be a positive number")
    @Min(value = 0, message = "price must be greater 0")
    private Double price;
    //private int id_business;
    //private int id_sale;
    //private int id_category;
    @Column(nullable = true)
    private Timestamp created_at;
    private Timestamp updated_at;
    @NotNull(message = "state must not be null")
    @Min(value = 0, message = "state must is (0,1,2)")
    @Max(value = 3, message = "state must is (0,1,2)")
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
