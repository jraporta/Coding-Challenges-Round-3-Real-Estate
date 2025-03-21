package com.round3.realestate.payload;

import lombok.Data;

@Data
public class RegistrationRequest {

    private String username;
    private String email;
    private String password;

}
