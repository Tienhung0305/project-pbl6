package com.example.sportstore06.dao.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartRequest {
    @NotNull(message = "id user must not be null")
    private Integer id_user;
    @NotNull(message = "id product must not be null")
    private Integer id_product;
    @NotNull(message = "quantity must not be null")
    private Integer quantity;
    private Timestamp created_at;
    private Timestamp updated_at;
}