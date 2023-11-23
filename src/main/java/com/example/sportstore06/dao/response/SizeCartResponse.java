package com.example.sportstore06.dao.response;

import com.example.sportstore06.model.Cart;
import com.example.sportstore06.model.SizeProduct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SizeCartResponse {
    private Integer id_cart;
    private SizeProduct size;
    public SizeCartResponse(Cart cart) {
        this.id_cart = cart.getId();
        this.size = cart.getSize();
    }
}
