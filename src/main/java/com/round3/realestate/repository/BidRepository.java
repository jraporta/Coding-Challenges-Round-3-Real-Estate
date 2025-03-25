package com.round3.realestate.repository;

import com.round3.realestate.entity.Auction;
import com.round3.realestate.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {

    List<Bid> findAllByAuctionId(Long auctionId);

    List<Bid> findAllByAuction(Auction auction);
}
