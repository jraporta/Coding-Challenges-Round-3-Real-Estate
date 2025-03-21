package com.round3.realestate.payload;

import lombok.Data;

@Data
public class LoginFailedResponse {

    private boolean success = false;
    private String error;

    public LoginFailedResponse(String error) {
        this.error = error;
    }
}
