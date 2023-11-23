package com.example.sportstore06.dao.response;

import com.example.sportstore06.model.Cart;
import com.example.sportstore06.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCartResponse {
    private Integer id_cart;
    private ProductResponse product;
    public ProductCartResponse(Cart cart) {
        this.id_cart = cart.getId();
        this.product = new ProductResponse(cart.getProduct());
    }
}
