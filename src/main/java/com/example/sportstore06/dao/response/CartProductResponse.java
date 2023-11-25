package com.example.sportstore06.dao.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartProductResponse {
    private Integer id_business;
    private ProductResponse productResponse;
}
