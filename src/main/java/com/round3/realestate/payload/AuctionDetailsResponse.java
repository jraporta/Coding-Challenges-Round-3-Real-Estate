package com.round3.realestate.payload;

import com.round3.realestate.entity.Auction;
import com.round3.realestate.entity.Bid;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AuctionDetailsResponse {

    private Auction auction;
    private List<Bid> bids;

}
