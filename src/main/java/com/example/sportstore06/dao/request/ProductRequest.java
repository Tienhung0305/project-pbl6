package com.example.sportstore06.dao.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    @NotNull(message = "id product information must not be null")
    private Integer id_product_information;
    @Min(value = 1,message = "price must greater than 1")
    private Double price;
    private String size;
    @Min(value = 1,message = "quantity must greater than 1")
    private Integer quantity;
}
