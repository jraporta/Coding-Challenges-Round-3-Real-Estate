package com.round3.realestate.service;

import com.round3.realestate.config.RabbitMQConfig;
import com.round3.realestate.entity.Auction;
import com.round3.realestate.entity.Bid;
import com.round3.realestate.entity.Property;
import com.round3.realestate.entity.User;
import com.round3.realestate.exception.AuctionException;
import com.round3.realestate.payload.AuctionCloseResponse;
import com.round3.realestate.payload.AuctionDetailsResponse;
import com.round3.realestate.repository.AuctionRepository;
import com.round3.realestate.repository.BidRepository;
import com.round3.realestate.repository.PropertyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AuctionServiceImp implements AuctionService{

    public static final String AUCTION_NOT_FOUND = "Auction not found.";
    public static final String UNAVAILABLE = "Unavailable";

    private final AuctionRepository auctionRepository;
    private final PropertyRepository propertyRepository;
    private final BidRepository bidRepository;
    private final RabbitMQBidProducer rabbitMQBidProducer;

    public AuctionServiceImp(AuctionRepository auctionRepository, PropertyRepository propertyRepository,
                             BidRepository bidRepository, RabbitMQBidProducer rabbitMQBidProducer) {
        this.auctionRepository = auctionRepository;
        this.propertyRepository = propertyRepository;
        this.bidRepository = bidRepository;
        this.rabbitMQBidProducer = rabbitMQBidProducer;
    }

    @Override
    public Auction createAuction(Long propertyId, LocalDateTime startTime, LocalDateTime endTime,
                                 BigDecimal minIncrement, BigDecimal startingPrice) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new AuctionException("Property not found."));
        validateProperty(property);
        property.setAvailability(UNAVAILABLE);
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
                .orElseThrow(() -> new AuctionException(AUCTION_NOT_FOUND));
        validateBid(auction, bidAmount);
        Bid bid = new Bid(
                null,
                bidAmount,
                LocalDateTime.now(),
                auction,
                user
        );
        rabbitMQBidProducer.sendMessage(bid);
        log.info("RabbitMQ: sent bid: {}", bid);
    }

    @Override
    public AuctionDetailsResponse getDetails(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionException(AUCTION_NOT_FOUND));
        List<Bid> bids = bidRepository.findAllByAuctionId(auctionId);
        return new AuctionDetailsResponse(auction, bids);
    }

    @Override
    public AuctionCloseResponse closeAuction(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionException(AUCTION_NOT_FOUND));
        auction.getProperty().setAvailability(UNAVAILABLE);
        auction.setStatus("closed");
        auctionRepository.save(auction);
        Optional<Bid> winningBid = bidRepository.findAllByAuction(auction).stream()
                .max(Comparator.comparing(Bid::getBidAmount));
        return winningBid.map(bid -> new AuctionCloseResponse(true, "Auction closed successfully.",
                auction.getCurrentHighestBid(), bid.getUser().getId()))
                .orElseGet(() -> new AuctionCloseResponse(true,
                        "Auction closed successfully.", null, null));
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME, concurrency = "1")
    private void bidConsumer(Bid bid){
        log.info("RabbitMQ: Received new bid {}", bid);
        if (bid.getAuction().getCurrentHighestBid().compareTo(bid.getBidAmount()) < 0) {
            bid.getAuction().setCurrentHighestBid(bid.getBidAmount());
            auctionRepository.save(bid.getAuction());
            bidRepository.save(bid);
        }
    }

    private void validateBid(Auction auction, BigDecimal bidAmount) {
        if ("closed".equalsIgnoreCase(auction.getStatus())) {
            throw new AuctionException("Auction is closed.");
        }
        if (auction.getMinIncrement().compareTo(bidAmount.subtract(auction.getCurrentHighestBid())) > 0) {
            throw new AuctionException("Bid amount must not exceed the established minimum increase.");
        }
    }

    private void validateProperty(Property property) {
        if (UNAVAILABLE.equalsIgnoreCase(property.getAvailability())) {
            throw new AuctionException("Property is not available.");
        }
    }
}
