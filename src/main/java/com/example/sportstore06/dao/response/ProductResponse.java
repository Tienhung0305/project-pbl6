package com.example.sportstore06.dao.response;

import com.example.sportstore06.entity.Image;
import com.example.sportstore06.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private int id;
    private Integer id_product_information;
    private String name_product_information;
    private String image_product_information;
    private Double price;
    private SaleResponse sale;
    private Integer quantity;
    private String size;

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.id_product_information = product.getProductInfo() != null ? product.getProductInfo().getId() : null;
        this.price = product.getPrice();
        this.quantity = product.getQuantity();
        this.size = product.getSize();
        this.name_product_information = product.getProductInfo() != null ? product.getProductInfo().getName() : null;
        List<Image> list = product.getProductInfo().getImageSet().stream().toList();
        for (Image image : list) {
            if (image.getIs_main()) {
                this.image_product_information = image.getUrl();
            }
        }
        this.sale = product.getProductInfo().getSale() != null ? new SaleResponse(product.getProductInfo().getSale()) : null;
    }
}
