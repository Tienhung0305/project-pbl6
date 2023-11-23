package com.example.sportstore06.dao.response;

import com.example.sportstore06.model.Business;
import com.example.sportstore06.model.Cart;
import com.example.sportstore06.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusinessCartResponse {
    private BusinessResponse business;
    private Set<ProductCartResponse> productSet;
    public BusinessCartResponse(Business business, Set<ProductCartResponse> products)
    {
        this.business = new BusinessResponse(business);
        this.productSet = products;
    }
}
