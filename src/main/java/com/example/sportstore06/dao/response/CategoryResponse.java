package com.example.sportstore06.dao.response;

import com.example.sportstore06.model.Category;
import com.example.sportstore06.model.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    private int id;
    private String name;
    private Timestamp created_at;
    private Timestamp updated_at;
    private Image image;
    public CategoryResponse(Category category)
    {
        this.id = category.getId();
        this.name = category.getName();
        this.created_at = category.getCreated_at();
        this.updated_at = category.getUpdated_at();
        this.image = category.getImage();
    }
}
