package com.example.sportstore06.dao.response;

import com.example.sportstore06.model.Business;
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
public class BusinessResponse {
    private Integer id;
    private String name;
    private String about;
    private Integer tax;
    private String payment;
    private Set<Integer> id_productInforeSet;
    public BusinessResponse(Business business) {
        this.id = business.getId();
        this.name = business.getName();
        this.about = business.getAbout();
        this.tax = business.getTax();
        this.payment = business.getPayment();
        this.id_productInforeSet = business.getProductInfoSet()
                .stream()
                .map(productInfor -> productInfor != null ? productInfor.getId() : null)
                .collect(Collectors.toSet());
    }
}
