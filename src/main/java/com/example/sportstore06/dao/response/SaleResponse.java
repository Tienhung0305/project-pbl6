package com.example.sportstore06.dao.response;

import com.example.sportstore06.model.Sale;
import jakarta.persistence.Column;
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
    private Integer id_business;
    private Timestamp started_at;
    private Timestamp ended_at;
    private String name;
    private String content;
    public SaleResponse(Sale sale) {
        this.id_business = sale.getId_business();
        this.started_at = sale.getStarted_at();
        this.ended_at = sale.getEnded_at();
        this.name = sale.getName();
        this.content = sale.getContent();
    }
}
