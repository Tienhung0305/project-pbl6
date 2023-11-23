package com.example.sportstore06.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    //private Integer id_user;
    //private Integer id_size;
    private Integer quantity;
    private Timestamp created_at;
    private Timestamp updated_at;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "id_size", nullable = false, referencedColumnName = "id")
    @JsonBackReference
    private SizeProduct size;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user", nullable = false, referencedColumnName = "id")
    @JsonBackReference
    private User user;

}
