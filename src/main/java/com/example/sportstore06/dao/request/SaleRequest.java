package com.example.sportstore06.dao.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleRequest {
    @NotNull(message = "id business must not be null")
    private Integer id_business;
    @Min(value = 0, message = "discount must be greater 0")
    private Long discount;
    @NotNull(message = "time start must not be null")
    private Timestamp started_at;
    @NotNull(message = "time end must not be null")
    private Timestamp ended_at;
    @NotNull(message = "name must not be null")
    private String name;
    private String content;
    private String url;
}
