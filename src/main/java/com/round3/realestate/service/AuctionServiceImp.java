package com.round3.realestate.service;

import com.round3.realestate.entity.Auction;
import com.round3.realestate.entity.Bid;
import com.round3.realestate.entity.Property;
import com.round3.realestate.entity.User;
import com.round3.realestate.exception.AuctionException;
import com.round3.realestate.repository.AuctionRepository;
import com.round3.realestate.repository.BidRepository;
import com.round3.realestate.repository.PropertyRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class AuctionServiceImp implements AuctionService{

    private final AuctionRepository auctionRepository;
    private final PropertyRepository propertyRepository;
    private final BidRepository bidRepository;

    public AuctionServiceImp(AuctionRepository auctionRepository, PropertyRepository propertyRepository,
                             BidRepository bidRepository) {
        this.auctionRepository = auctionRepository;
        this.propertyRepository = propertyRepository;
        this.bidRepository = bidRepository;
    }

    @Override
    public Auction createAuction(Long propertyId, LocalDateTime startTime, LocalDateTime endTime,
                                 BigDecimal minIncrement, BigDecimal startingPrice) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new AuctionException("Property not found."));
        validateProperty(property);
        Auction auction = new Auction(
                null,
                startingPrice,
                endTime,
                minIncrement,
                startTime,
                startingPrice,
                "open",
                property
                );
        return auctionRepository.save(auction);
    }

    @Override
    public void placeBid(Long auctionId, BigDecimal bidAmount, User user) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionException("Auction not found."));
        validateBid(auction, bidAmount);
        auction.setCurrentHighestBid(auction.getCurrentHighestBid().add(bidAmount));
        Bid bid = new Bid(
                null,
                bidAmount,
                LocalDateTime.now(),
                auction,
                user
        );
        bidRepository.save(bid);
    }

    private void validateBid(Auction auction, BigDecimal bidAmount) {
        if ("closed".equalsIgnoreCase(auction.getStatus())) {
            throw new AuctionException("Auction is closed.");
        }
        if (auction.getMinIncrement().compareTo(bidAmount) > 0) {
            throw new AuctionException("Bid amount must not exceed the established minimum increase.");
        }
    }

    private void validateProperty(Property property) {
        if ("Unavailable".equalsIgnoreCase(property.getAvailability())) {
            throw new AuctionException("Property is not available.");
        }
    }
}
