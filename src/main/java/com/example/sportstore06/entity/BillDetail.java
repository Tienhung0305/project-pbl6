package com.example.sportstore06.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
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
    @Min(value = 0)
    private Integer quantity;
    @Min(value = 0)
    private double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_bill", nullable = false, referencedColumnName = "id")
    @JsonBackReference
    private Bill bill;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "id_product", nullable = false, referencedColumnName = "id")
    @JsonBackReference
    private Product product;
}
