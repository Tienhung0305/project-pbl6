package com.example.sportstore06.dao.response;

import com.example.sportstore06.model.Business;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusinessCartResponse {
    private BusinessResponse business;
    private Set<SizeCartResponse> productSet;
    public BusinessCartResponse(Business business, Set<SizeCartResponse> products)
    {
        this.business = new BusinessResponse(business);
        this.productSet = products
                .stream()
                .sorted(Comparator.comparingInt(SizeCartResponse::getId_cart))
                .collect(Collectors.toSet());
    }
}
