package com.example.sportstore06.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "payment")
public class Payment {
    @Id
    private String id;
    private String partner_code;
    private String access_key;
    private String secret_key;
}
