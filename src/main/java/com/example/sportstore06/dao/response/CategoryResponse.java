package com.example.sportstore06.dao.response;

import com.example.sportstore06.model.Category;
import com.example.sportstore06.model.Group;
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
    private Integer group_id;
    private String group_name;
    public CategoryResponse(Category category)
    {
        this.id = category.getId();
        this.name = category.getName();
        this.group_id = category.getGroup().getId();
        this.group_name = category.getGroup().getName();
    }
}
