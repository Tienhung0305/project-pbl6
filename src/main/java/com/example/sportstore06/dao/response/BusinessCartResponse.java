package com.example.sportstore06.dao.response;

import com.example.sportstore06.model.Business;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusinessCartResponse {
    private BusinessResponse business;
    private Set<SizeCartResponse> productSet = new LinkedHashSet<>();
    public BusinessCartResponse(Business business, Set<SizeCartResponse> products)
    {
        this.business = new BusinessResponse(business);
        this.productSet = products;
    }
}
