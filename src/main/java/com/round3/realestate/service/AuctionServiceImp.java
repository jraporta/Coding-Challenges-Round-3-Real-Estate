package com.round3.realestate.service;

import com.round3.realestate.entity.Auction;
import com.round3.realestate.entity.Property;
import com.round3.realestate.exception.AuctionException;
import com.round3.realestate.repository.AuctionRepository;
import com.round3.realestate.repository.PropertyRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class AuctionServiceImp implements AuctionService{

    private final AuctionRepository auctionRepository;
    private final PropertyRepository propertyRepository;

    public AuctionServiceImp(AuctionRepository auctionRepository, PropertyRepository propertyRepository) {
        this.auctionRepository = auctionRepository;
        this.propertyRepository = propertyRepository;
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

    private void validateProperty(Property property) {
        if ("Unavailable".equalsIgnoreCase(property.getAvailability())) {
            throw new AuctionException("Property is not available.");
        }
    }
}
