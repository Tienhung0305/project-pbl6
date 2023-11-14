package com.example.sportstore06.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Timestamp;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    //private Integer id_user;
    //private Integer id_product;
    @NotNull
    private Integer quantity;
    private Timestamp created_at;
    private Timestamp updated_at;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "id_product", nullable = false, referencedColumnName = "id")
    @JsonBackReference
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user", nullable = false, referencedColumnName = "id")
    @JsonBackReference
    private User user;
}
