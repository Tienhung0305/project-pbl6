package com.example.sportstore06.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "bill_details")
public class BillDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    //private int id_bill;
    //private int id_product;
    private Integer quantity;
    private Integer price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_bill", nullable = false, referencedColumnName = "id")
    @JsonBackReference
    private Bill bill;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_product", nullable = false, referencedColumnName = "id")
    @JsonBackReference
    private Product product;
}
