package com.example.sportstore06.dao.response;

import com.example.sportstore06.model.Product;
import com.example.sportstore06.model.SizeProduct;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SizeProductResponse {
    private int id;
    private ProductResponse product;
    private Integer quantity;
    private String size;

    public SizeProductResponse(SizeProduct sizeProduct)
    {
        this.id = sizeProduct.getId();
        this.product = new ProductResponse(sizeProduct.getProduct());
        this.quantity = sizeProduct.getQuantity();
        this.size = sizeProduct.getSize();
    }
}
