package com.example.sportstore06.dao.request;

import com.example.sportstore06.model.Product;
import com.example.sportstore06.model.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
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
    @NotNull(message = "id size must not be null")
    private Integer id_size;
    @NotNull(message = "quantity must not be null")
    private Integer quantity;
    private Timestamp created_at;
    private Timestamp updated_at;
}
