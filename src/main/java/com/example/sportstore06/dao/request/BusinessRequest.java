package com.example.sportstore06.dao.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusinessRequest {
    @NotNull(message = "id must not be null")
    private Integer id_user;
    @NotBlank(message = "name must not be blank")
    @Size(min = 1, max = 50, message = "name must be between 1 and 50 characters")
    private String name;
    private String payment;
    private String about;
    private Integer tax;
}
