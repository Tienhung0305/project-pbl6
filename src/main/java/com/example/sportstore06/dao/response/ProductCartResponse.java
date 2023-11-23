package com.example.sportstore06.dao.response;

import com.example.sportstore06.model.Cart;
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
public class ProductCartResponse {
    private Integer id_cart;
    private ProductResponse product;
    public ProductCartResponse(Product product, Cart cart) {
        this.id_cart = cart.getId();
        this.product = new ProductResponse(product);
    }
}
