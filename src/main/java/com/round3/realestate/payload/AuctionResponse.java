package com.round3.realestate.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuctionResponse {

    private String message;
    private Long auctionId;
    private Boolean success;

}
