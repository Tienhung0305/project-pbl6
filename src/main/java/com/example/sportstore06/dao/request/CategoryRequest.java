package com.example.sportstore06.dao.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {
    @NotBlank(message = "name must not be blank")
    @Size(min = 1, max = 100, message = "name must be between 1 and 100 characters")
    private String name;
    @NotNull(message = "id category group must not be null")
    private Integer category_group_id;
}
