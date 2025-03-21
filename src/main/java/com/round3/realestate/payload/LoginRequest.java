package com.round3.realestate.payload;

import lombok.Data;

@Data
public class LoginRequest {

    private String usernameOrEmail;
    private String password;

}
