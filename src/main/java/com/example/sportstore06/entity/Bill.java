package com.example.sportstore06.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    private Long total;
    //private int id_user;
    //private int id_transaction;
    @NotNull
    private int id_business;
    private Timestamp created_at;
    private Timestamp updated_at;
    private Integer state;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user", nullable = false, referencedColumnName = "id")
    @JsonBackReference
    private User user;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_transaction", nullable = true, referencedColumnName = "id")
    @JsonManagedReference
    private Transaction transaction;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<BillDetail> bill_detailSet = new HashSet<>();

    @OneToOne(mappedBy = "bill",cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JsonManagedReference
    private Comment comment;

}
