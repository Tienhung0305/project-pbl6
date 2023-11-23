package com.example.sportstore06.model;

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
@Table(name = "sizes")
public class SizeProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    //private Integer id_product;
    @Min(value = 1)
    private Integer quantity;
    private String size;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "id_product", nullable = false, referencedColumnName = "id")
    @JsonBackReference
    private Product product;

    @OneToMany(mappedBy = "size", fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<Cart> cartSet = new HashSet<>();
}
