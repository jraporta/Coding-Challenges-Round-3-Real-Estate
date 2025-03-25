package com.round3.realestate.payload;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AuctionRequest {

    private Long propertyId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal minIncrement;
    private BigDecimal startingPrice;

}
