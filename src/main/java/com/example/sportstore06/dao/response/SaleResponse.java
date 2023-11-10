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
    private int id;
    private Integer id_business;
    private Timestamp started_at;
    private Timestamp ended_at;
    private String name;
    private String content;
    private Integer id_image;

    public SaleResponse(Sale sale) {
        this.id = sale.getId();
        this.id_business = sale.getId_business();
        this.started_at = sale.getStarted_at();
        this.ended_at = sale.getEnded_at();
        this.name = sale.getName();
        this.content = sale.getContent();
        this.id_image = sale.getImage() == null ? null : sale.getImage().getId();
    }
}
