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
    Integer state;
    public MomoResponse() {
    }
    public MomoResponse (String resultCode, String message, Integer state)
    {
        this.resultCode = resultCode;
        this.message = message;
        this.state = state;
    }
}
