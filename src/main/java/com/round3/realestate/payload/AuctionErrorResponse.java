package com.round3.realestate.payload;

import lombok.Data;

@Data
public class AuctionErrorResponse {

    private boolean success = false;
    private String error;

    public AuctionErrorResponse(String error) {
        this.error = error;
    }
}
