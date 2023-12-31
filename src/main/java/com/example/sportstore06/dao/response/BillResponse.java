package com.example.sportstore06.dao.response;

import com.example.sportstore06.entity.Bill;
import com.example.sportstore06.entity.BillDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class BillResponse {
    private int id;
    private String name;
    private String information;
    private Long total;
    private Integer id_user;
    private Timestamp created_at;
    private Timestamp updated_at;
    private Integer id_business;
    private Integer state;
    private TransactionResponse transaction;
    private Set<BillDetailResponse> bill_detailSet = new HashSet<>();
    private Boolean is_rating;

    public BillResponse(Bill bill) {
        this.id = bill.getId();
        this.name = bill.getName();
        this.information = bill.getInformation();
        this.total = bill.getTotal();
        this.id_user = bill.getUser() != null ? bill.getUser().getId() : null;
        this.created_at = bill.getCreated_at();
        this.updated_at = bill.getUpdated_at();
        this.state = bill.getState();
        Set<BillDetail> billDetailSet = bill.getBill_detailSet();
        Stream<BillDetailResponse> response = billDetailSet
                .stream()
                .map(billDetail -> billDetail != null ? new BillDetailResponse(billDetail) : null);
        this.bill_detailSet = response.collect(Collectors.toSet());
        this.id_business = bill.getId_business();
        this.transaction = bill.getTransaction() != null ? new TransactionResponse(bill.getTransaction()) : null;
        if (bill.getCommentSet() != null && bill.getCommentSet().size() != 0) {
            this.is_rating = true;
        } else {
            this.is_rating = false;
        }
    }
}
