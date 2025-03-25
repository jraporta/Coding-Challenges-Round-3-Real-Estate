package com.round3.realestate.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class AuctionCloseResponse {

    private Boolean success;
    private String message;
    private BigDecimal winningBid;
    private Long winningUserId;

}
