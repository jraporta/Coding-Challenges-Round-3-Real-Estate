package com.round3.realestate.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BidResponse {

    private String message;
    private Boolean success;

}
