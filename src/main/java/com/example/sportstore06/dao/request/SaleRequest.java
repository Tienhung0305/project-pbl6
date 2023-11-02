package com.example.sportstore06.dao.request;

import com.example.sportstore06.model.Image;
import com.example.sportstore06.model.Product;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleRequest {
    @NotNull(message = "id business must not be null")
    private Integer id_business;
    @NotNull(message = "time start must not be null")
    private Timestamp started_at;
    @NotNull(message = "time end must not be null")
    private Timestamp ended_at;
    @NotNull(message = "name must not be null")
    private String name;
    private String content;
    @NotNull(message = "id_image must not be null")
    private Integer id_image;
}
