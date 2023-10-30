package com.example.sportstore06.dao.request;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillDetailRequest {
    @NotNull(message = "id product must not be null")
    private Integer id_product;
    @Min(value = 0, message = "quantity must be greater 0")
    private Integer quantity;
    @Min(value = 0, message = "price must be greater 0")
    private Double price;
}
