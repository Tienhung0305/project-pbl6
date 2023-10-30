package com.example.sportstore06.dao.request;

import com.example.sportstore06.model.BillDetail;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class BillRequest {
    private String name;
    private String information;
    @Min(value = 0, message = "total must be greater 0")
    private Double total;
    @NotNull(message = "id user must not be null")
    private Integer id_user;
    private Timestamp created_at;
    private Timestamp updated_at;
    @NotNull(message = "state must not be null")
    private Boolean state_null;
    @NotNull(message = "bill details must not be null")
    private Set<BillDetailRequest> bill_detailSet = new HashSet<>();
}
