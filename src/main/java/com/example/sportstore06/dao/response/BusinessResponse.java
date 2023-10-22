package com.example.sportstore06.dao.response;

import com.example.sportstore06.model.Business;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusinessResponse {
    private Integer id;
    private String name;
    private Timestamp created_at;
    private Timestamp updated_at;
    private Integer state;
    private Integer id_image;

    public BusinessResponse(Business business) {
        this.id = business.getId();
        this.name = business.getName();
        this.created_at = business.getCreated_at();
        this.updated_at = business.getUpdated_at();
        this.state = business.getState();
        this.id_image = business.getImage() != null ? business.getImage().getId() : null;
    }
}
