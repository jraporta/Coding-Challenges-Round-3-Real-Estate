package com.round3.realestate.payload;

import lombok.Data;

@Data
public class MortgageExceptionResponse {

    private boolean success = false;
    private String error;

    public MortgageExceptionResponse(String error) {
        this.error = error;
    }
}
