package com.example.sportstore06.dao.response;

import com.example.sportstore06.model.Cart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    private Integer id;
    private UserResponse user;
    private ProductResponse product;
    private BusinessResponse business;
    private Integer quantity;
    private Timestamp created_at;
    private Timestamp updated_at;

    public CartResponse(Cart cart)
    {
        this.id = cart.getId();
        this.user = cart.getUser() != null ? new UserResponse(cart.getUser()) :null;
        this.product = cart.getProduct() != null ? new ProductResponse(cart.getProduct()) : null;
        this.quantity = cart.getQuantity();
        this.created_at = cart.getCreated_at();
        this.updated_at = cart.getUpdated_at();
        this.business = new BusinessResponse(cart.getProduct().getProductInfo().getBusiness());
    }
}
