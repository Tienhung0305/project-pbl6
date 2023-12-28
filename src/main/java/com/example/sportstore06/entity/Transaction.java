package com.example.sportstore06.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "transactions")
public class Transaction {
    @Id
    private int id;
    private String payUrl;
    private String orderId;
    private String requestId;
    private String transId;
    private String payType;
    private String orderType;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Bill> billSet = new HashSet<>();
}
