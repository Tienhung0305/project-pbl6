package com.example.sportstore06.dao.response;

import com.example.sportstore06.model.Bill;
import com.example.sportstore06.model.BillDetail;
import com.example.sportstore06.model.Product;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class BillResponse {
    private int id;
    private String name;
    private String information;
    private Double total;
    private Integer id_user;
    private Timestamp created_at;
    private Timestamp updated_at;
    private Boolean state_null;
    private Set<BillDetail> bill_detailSet = new HashSet<>();

    public BillResponse(Bill bill) {
        this.id = bill.getId();
        this.name = bill.getName();
        this.information = bill.getInformation();
        this.total = bill.getTotal();
        this.id_user = bill.getUser() != null ? bill.getUser().getId() : null;
        this.created_at = bill.getCreated_at();
        this.updated_at = bill.getUpdated_at();
        this.state_null = bill.getState_null();
        this.bill_detailSet = bill.getBill_detailSet();
    }
}
