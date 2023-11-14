package com.example.sportstore06.dao.response;

import com.example.sportstore06.model.Cart;
import jakarta.validation.constraints.NotNull;
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
    private UserResponse userResponse;
    private ProductResponse productResponse;
    private Integer quantity;
    private Timestamp created_at;
    private Timestamp updated_at;

    public CartResponse(Cart cart)
    {
        this.userResponse = new UserResponse(cart.getUser());
        this.productResponse = new ProductResponse(cart.getProduct());
        this.quantity = cart.getQuantity();
        this.created_at = cart.getCreated_at();
        this.updated_at = cart.getUpdated_at();
    }
}
