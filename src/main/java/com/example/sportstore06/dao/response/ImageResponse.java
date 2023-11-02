package com.example.sportstore06.dao.response;

import com.example.sportstore06.model.Image;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImageResponse {
    private int id;
    private String name;
    private Timestamp created_at;
    private Timestamp updated_at;
    private String url;
    private Integer id_category;
    private Integer id_user;
    private Integer id_business;
    private Integer id_product;
    private Integer id_comment;
    private Integer id_sale;

    public ImageResponse(Image image) {
        this.id = image.getId();
        this.name = image.getName();
        this.created_at = image.getCreated_at();
        this.updated_at = image.getUpdated_at();
        this.url = image.getUrl();
        this.id_category = image.getCategory() == null ? null : image.getCategory().getId();
        this.id_user = image.getUser() == null ? null : image.getUser().getId();
        this.id_business = image.getBusiness() == null ? null : image.getBusiness().getId();
        this.id_product = image.getProduct() == null ? null : image.getProduct().getId();
        this.id_comment = image.getComment() == null ? null : image.getComment().getId();
        this.id_sale = image.getSale() == null ? null : image.getSale().getId();
    }
}
