package com.round3.realestate.payload;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BidRequest {

    private BigDecimal bidAmount;

}
