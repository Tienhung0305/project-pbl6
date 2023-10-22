package com.example.sportstore06.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "bills")
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String information;
    private Double total;
    //private int id_user;
    private Timestamp created_at;
    private Timestamp updated_at;
    private Boolean state_null;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user", nullable = false, referencedColumnName = "id")
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<BillDetail> bill_detailSet = new HashSet<>();
}
