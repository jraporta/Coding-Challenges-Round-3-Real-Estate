package com.round3.realestate.service;

import com.round3.realestate.entity.Auction;
import com.round3.realestate.entity.User;
import com.round3.realestate.payload.AuctionDetailsResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface AuctionService {

    Auction createAuction(Long propertyId, LocalDateTime startTime, LocalDateTime endTime, BigDecimal minIncrement,
                          BigDecimal startingPrice);

    void placeBid(Long auctionId, BigDecimal bidAmount, User user);

    AuctionDetailsResponse getDetails(Long auctionId);
}
