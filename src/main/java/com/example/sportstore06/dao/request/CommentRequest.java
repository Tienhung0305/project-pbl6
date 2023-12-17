package com.example.sportstore06.dao.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {
    @NotNull(message = "id product information must not be null")
    private Integer id_product_information;
    @NotBlank
    @Length(min = 1, max = 200)
    private String content;
    private Integer reply;
    @NotNull(message = "id user must not be null")
    private Integer id_user;
    private Timestamp created_at;
    private Timestamp updated_at;
    private Boolean is_like;
    private Set<Integer> id_imageSet = new HashSet<>();
}
