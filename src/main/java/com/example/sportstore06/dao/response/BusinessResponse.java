package com.example.sportstore06.dao.response;

import com.example.sportstore06.model.Business;
import com.example.sportstore06.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusinessResponse {
    private Integer id;
    private String name;
    private String about;
    private Integer tax;
    private Set<ProductResponse> productSet;
    public BusinessResponse(Business business) {
        this.id = business.getId();
        this.name = business.getName();
        this.about = business.getAbout();
        this.tax = business.getTax();
        this.productSet = business.getProductSet().stream().map(
                product -> new ProductResponse(product)).collect(Collectors.toSet());
    }
}
