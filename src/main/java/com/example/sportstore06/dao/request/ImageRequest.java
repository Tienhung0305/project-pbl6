package com.example.sportstore06.dao.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageRequest {
    private String name;
    private Timestamp created_at;
    private Timestamp updated_at;
    private Boolean is_main;
    @NotBlank(message = "url must not be blank")
    private String url;
}
