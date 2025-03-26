package com.round3.realestate.payload;

import com.round3.realestate.entity.User;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BidRabbitMQPayload {

    Long auctionId;

    BigDecimal bidAmount;

    User user;

}
