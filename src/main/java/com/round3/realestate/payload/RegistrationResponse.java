package com.round3.realestate.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RegistrationResponse {

    private Boolean success;
    private String message;

}
