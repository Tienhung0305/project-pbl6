package com.example.sportstore06.dao.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {
    private int id;
    private Integer id_product;
    private String content;
    private Boolean report;
    private Integer reply;
    private Integer id_user;
    private Timestamp created_at;
    private Timestamp updated_at;
}
