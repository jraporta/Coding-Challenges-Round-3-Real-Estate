package com.round3.realestate.controller;

import com.round3.realestate.entity.Auction;
import com.round3.realestate.payload.AuctionRequest;
import com.round3.realestate.payload.AuctionResponse;
import com.round3.realestate.service.AuctionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auction")
public class AuctionController {

    private final AuctionService auctionService;

    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @PostMapping("/create")
    public ResponseEntity<AuctionResponse> createAuction(@RequestBody AuctionRequest req) {
        Auction auction = auctionService.createAuction(req.getPropertyId(), req.getStartTime(), req.getEndTime(),
                req.getMinIncrement(), req.getStartingPrice());
        return ResponseEntity.ok(new AuctionResponse("Auction created successfully.", auction.getId(), true));
    }

}
