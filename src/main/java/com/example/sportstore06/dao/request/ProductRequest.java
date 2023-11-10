package com.example.sportstore06.dao.request;

import com.example.sportstore06.model.Image;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    @NotBlank(message = "name must not be blank")
    @Size(min = 1, max = 100, message = "name must be between 1 and 100 characters")
    private String name;
    private String detail;
    @Min(value = 0, message = "price must be greater 0")
    private Double price;
    private String attribute;
    private String brand;
    @Min(value = 0, message = "quantity must be greater 0")
    private Integer quantity;
    @NotNull(message = "id business must not be null")
    private Integer id_business;
    private Integer id_sale;
    private Integer id_category;
    private Timestamp created_at;
    private Timestamp updated_at;
    @NotNull(message = "state must not be null")
    @Min(value = 0, message = "state must is (0,1,2)")
    @Max(value = 3, message = "state must is (0,1,2)")
    private Integer state;

    private Set<Integer> id_imageSet = new HashSet<>();
}
