package com.example.sportstore06.dao.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartProductResponse {
    private Integer id_business;
    private ProductResponse productResponse;
    private Integer quantity;
    private Timestamp created_at;
    private Timestamp updated_at;
}
