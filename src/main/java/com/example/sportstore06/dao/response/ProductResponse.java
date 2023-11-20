package com.example.sportstore06.dao.response;

import com.example.sportstore06.model.Category;
import com.example.sportstore06.model.Image;
import com.example.sportstore06.model.Product;
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
public class ProductResponse {
    private int id;
    private String name;
    private String detail;
    private Double price;
    private String brand;
    private String attribute;
    private Integer id_business;
    private Integer id_sale;
    private Set<Category> categorySet = new HashSet<>();
    private Timestamp created_at;
    private Timestamp updated_at;
    private Set<Image> imageSet = new HashSet<>();
    private Integer state;

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.detail = product.getDetail();
        this.price = product.getPrice();
        Set<Category> categorySet = product.getCategorySet();
        for (Category category : categorySet) {
            if (category.getGroup().getId() == 5) {
                this.brand = category.getName();
            }
        }
        this.attribute = product.getAttribute();
        this.id_business = product.getBusiness() != null ? product.getBusiness().getId() : null;
        this.id_sale = product.getSale() != null ? product.getSale().getId() : null;
        this.categorySet = product.getCategorySet();
        this.created_at = product.getCreated_at();
        this.updated_at = product.getUpdated_at();
        this.state = product.getState();
        this.imageSet = product.getImageSet();
    }
}
