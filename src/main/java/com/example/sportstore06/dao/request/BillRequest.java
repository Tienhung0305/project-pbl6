package com.example.sportstore06.dao.request;

import com.example.sportstore06.model.BillDetail;
import jakarta.persistence.*;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import lombok.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class BillRequest {
    private int id;
    private String name;
    private String information;
    private Double total;
    @NotNull(message = "id user must not be null")
    @Positive(message = "id user must be a positive number")
    private Integer id_user;
    private Timestamp created_at;
    private Timestamp updated_at;
    @NotNull(message = "state must not be null")
    private Boolean state_null;
    private Set<BillDetailRequest> bill_detailSet = new HashSet<>();
}
