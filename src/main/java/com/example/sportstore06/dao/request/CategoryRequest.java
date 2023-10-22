package com.example.sportstore06.dao.request;

import com.example.sportstore06.model.Image;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {
    private int id;
    @NotBlank(message = "name must not be blank")
    @Size(min = 1, max = 100, message = "name must be between 1 and 100 characters")
    private String name;
    private Timestamp created_at;
    private Timestamp updated_at;
    private Integer id_image;
}
