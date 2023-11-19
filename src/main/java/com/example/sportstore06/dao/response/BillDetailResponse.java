package com.example.sportstore06.dao.response;

import com.example.sportstore06.model.BillDetail;
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
    private Integer id_product;
    private Integer quantity;
    private Integer price;

    public BillDetailResponse(BillDetail billDetail){
        this.id = billDetail.getId();
        this.id_bill = billDetail.getBill().getId();
        this.id_product = billDetail.getProduct().getId();
        this.quantity = billDetail.getQuantity();
        this.price = billDetail.getPrice();
    }
}
