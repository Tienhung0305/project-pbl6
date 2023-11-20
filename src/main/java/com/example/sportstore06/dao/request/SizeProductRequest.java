package com.example.sportstore06.dao.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SizeProductRequest {
    @NotNull(message = "id product must not be null")
    private Integer id_product;
    private String size;
    @Min(value = 1,message = "quantity must greater than 1")
    private Integer quantity;
    private String color;
}
