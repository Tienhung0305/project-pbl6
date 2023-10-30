package com.example.sportstore06.dao.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {
    @NotNull(message = "id product must not be null")
    private Integer id_product;
    @NotBlank
    @Length(min = 1, max = 200)
    private String content;
    @NotNull(message = "report must not be null")
    private Boolean report;
    private Integer reply;
    @NotNull(message = "id user must not be null")
    private Integer id_user;
    private Timestamp created_at;
    private Timestamp updated_at;
}
