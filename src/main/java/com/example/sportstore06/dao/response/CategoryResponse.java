package com.example.sportstore06.dao.response;

import com.example.sportstore06.model.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    private int id;
    private String name;
    private Integer id_category_group;
    private String name_category_group;

    public CategoryResponse(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.id_category_group = category.getCategoryGroup() != null ?
                category.getCategoryGroup().getId() : null;
        this.name_category_group = category.getCategoryGroup() != null ?
                category.getCategoryGroup().getName() : null;
    }
}
