package com.example.sportstore06.service.MomoService.Model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private String id;
    private String name;
    private String description;
    private String imageUrl;
    private String manufacturer;
    private Long price;
    private String currency;
    private Integer quantity;
    private Long totalPrice;
}
