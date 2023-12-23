package com.example.sportstore06.dao.response;

import com.example.sportstore06.entity.BillDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillDetailResponse {
    private int id;
    private Integer id_bill;
    private ProductResponse product;
    private Integer quantity;
    private Double price;

    public BillDetailResponse(BillDetail billDetail) {
        this.id = billDetail.getId();
        this.id_bill = billDetail.getBill().getId();
        this.product = billDetail.getProduct() != null ? new ProductResponse(billDetail.getProduct()) : null;
        this.quantity = billDetail.getQuantity();
        this.price = billDetail.getPrice();
    }
}
