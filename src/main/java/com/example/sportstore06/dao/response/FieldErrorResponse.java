package com.example.sportstore06.dao.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class FieldErrorResponse {
    private String field;
    private String message;
}
