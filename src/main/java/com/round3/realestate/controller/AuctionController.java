package com.round3.realestate.controller;

import com.round3.realestate.entity.Auction;
import com.round3.realestate.entity.User;
import com.round3.realestate.payload.AuctionRequest;
import com.round3.realestate.payload.AuctionResponse;
import com.round3.realestate.payload.BidRequest;
import com.round3.realestate.payload.BidResponse;
import com.round3.realestate.service.AuctionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/{auctionId}/bid")
    public ResponseEntity<BidResponse> placeBid(@PathVariable Long auctionId,
                                                @RequestBody BidRequest req,
                                                @AuthenticationPrincipal User user) {
        auctionService.placeBid(auctionId, req.getBidAmount(), user);
        return ResponseEntity.ok(new BidResponse("Bid submitted successfully.", true));
    }

}
