package com.example.sportstore06.dao.request;

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
public class ProductInfoRequest {
    @NotBlank(message = "name must not be blank")
    @Size(min = 1, max = 100, message = "name must be between 1 and 100 characters")
    private String name;
    private String detail;
    private String attribute;
    @NotNull(message = "id business must not be null")
    private Integer id_business;
    private Integer id_sale;
    private Timestamp created_at;
    private Timestamp updated_at;
    private Set<Integer> id_categorySet;
    private Set<Integer> id_imageSet;
}
