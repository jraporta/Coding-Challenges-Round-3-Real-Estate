package com.round3.realestate.payload;

import lombok.Data;

@Data
public class LoginSuccessfulResponse {

    private String accessToken;
    private String tokenType = "Bearer";

    public LoginSuccessfulResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
