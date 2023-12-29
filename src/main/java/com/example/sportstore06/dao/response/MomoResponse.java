package com.example.sportstore06.dao.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class MomoResponse {
    String resultCode;
    String message;
    public MomoResponse() {
    }
    public MomoResponse (String resultCode, String message)
    {
        this.resultCode = resultCode;
        this.message = message;
    }
}
