package com.example.sportstore06.dao.response;

import com.example.sportstore06.entity.Sale;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaleResponse {
    private int id;
    private BusinessResponse businessResponse;
    private Long discount;
    private Timestamp started_at;
    private Timestamp ended_at;
    private String name;
    private String content;
    private String url;
    public SaleResponse(Sale sale) {
        this.id = sale.getId();
        this.businessResponse = sale.getBusiness() != null ? new BusinessResponse(sale.getBusiness()) : null;
        this.discount = sale.getDiscount();
        this.started_at = sale.getStarted_at();
        this.ended_at = sale.getEnded_at();
        this.name = sale.getName();
        this.content = sale.getContent();
        this.url = sale.getUrl();
    }
}
