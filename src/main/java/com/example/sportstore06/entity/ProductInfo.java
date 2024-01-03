package com.example.sportstore06.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "product_informations")
public class ProductInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank
    @Length(min = 1, max = 100)
    private String name;
    private String detail;
    private String attribute;

    //private int id_business;
    //private int id_sale;
    private Timestamp created_at;
    private Timestamp updated_at;
    @NotNull
    @Min(value = 0)
    @Max(value = 3)
    private Integer state;
    private Integer state_before;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_business", nullable = false, referencedColumnName = "id")
    @JsonBackReference
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sale", referencedColumnName = "id")
    @JsonBackReference
    private Sale sale;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "product_categories",
            joinColumns = @JoinColumn(name = "id_product_information"),
            inverseJoinColumns = @JoinColumn(name = "id_category")
    )
    @JsonManagedReference
    private Set<Category> categorySet = new HashSet<>();

    @OneToMany(mappedBy = "productInfo", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Image> imageSet = new HashSet<>();

    @OneToMany(mappedBy = "productInfo", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Product> productSet = new HashSet<>();

    @OneToMany(mappedBy = "productInfo", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Comment> commentSet = new HashSet<>();
}
