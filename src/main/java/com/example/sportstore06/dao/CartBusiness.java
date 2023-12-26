package com.example.sportstore06.dao;

import com.example.sportstore06.entity.Business;
import com.example.sportstore06.entity.Cart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class CartBusiness{
    Business business;
    Set<Cart> cartSet;

    public CartBusiness(Business business, Set<Cart> cartSet)
    {
        this.business = business;
        this.cartSet = cartSet;
    }
}
